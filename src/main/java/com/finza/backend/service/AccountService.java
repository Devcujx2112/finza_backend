package com.finza.backend.service;

import com.finza.backend.config.GoogleConfig;
import com.finza.backend.constant.BaseMessage;
import com.finza.backend.constant.StatusCode;
import com.finza.backend.dto.request.*;
import com.finza.backend.dto.response.AccountResponse;
import com.finza.backend.dto.response.AuthResponse;
import com.finza.backend.entity.*;
import com.finza.backend.exception.AppException;
import com.finza.backend.mapper.AccountMapper;
import com.finza.backend.repository.Account_repository;
import com.finza.backend.repository.Authentication_repository;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.Version;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {

    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

    private final GoogleConfig googleConfig;

    @Value("${facebook.app-id}")
    private String facebookAppId;

    @Value("${facebook.app-secret}")
    private String facebookAppSecret;

    private final Account_repository accountRepository;
    private final Authentication_repository authenticationRepository;
    private final AccountMapper accountMapper;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AccountResponse register(AccountRequest request) {
        if (accountRepository.existsByEmail(request.getEmail())) {
            throw new AppException(StatusCode.EmailAlreadyExists, BaseMessage.EMAIL_EXISTED);
        }
        if (accountRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new AppException(StatusCode.PhoneAlreadyExists, BaseMessage.PHONE_EXISTED);
        }

        Account account = accountMapper.toEntity(request);
        account.setPassword(passwordEncoder.encode((request.getPassword())));
        account.setRole(AccountRole.CUSTOMER);
        account.setAccountTier(AccountTier.Free);

        accountRepository.save(account);
        return accountMapper.toResponse(account);
    }

    public AuthResponse registerTrial() {
        String randomEmail = "guest_" + UUID.randomUUID().toString().substring(0, 6);
        String randomPassword = UUID.randomUUID().toString();

        Account account = new Account();
        account.setEmail(randomEmail);
        account.setPassword(passwordEncoder.encode(randomPassword));
        account.setRole(AccountRole.CUSTOMER);
        account.setAccountTier(AccountTier.TRIAL);
        account.setTrialExpiredAt(LocalDate.now().plusDays(3));
        accountRepository.save(account);

        String accessToken = jwtService.generateAccessToken(randomEmail);
        String refreshToken = jwtService.generateRefreshToken(randomEmail);

        saveRefreshToken(account, refreshToken);
        return buildAuthResponse(account, accessToken, refreshToken);
    }

    public AuthResponse login(LoginRequest loginRequest) {
        Account account = accountRepository.findByEmail(loginRequest.getEmail()).orElseThrow(() ->
                new AppException(StatusCode.EmailNotExists, BaseMessage.ACCOUNT_NOT_FOUND));

        if (account.getPassword() == null) {
            throw new AppException(StatusCode.PasswordNotNull, BaseMessage.SOCIAL_ACCOUNT_NO_PASSWORD);
        }

        if (!passwordEncoder.matches(loginRequest.getPassword(), account.getPassword())) {
            throw new AppException(StatusCode.PasswordNotCorrect, BaseMessage.WRONG_PASSWORD);
        }

        String accessToken = jwtService.generateAccessToken(account.getEmail());
        String refreshToken = jwtService.generateRefreshToken(account.getEmail());

        saveRefreshToken(account, refreshToken);
        return buildAuthResponse(account, accessToken, refreshToken);
    }

    public AuthResponse loginWithSocial(SocialLogin loginRequest) {
        return switch (loginRequest.getProvider()) {
            case GOOGLE -> handleGoogleLogin(loginRequest);
            case FACEBOOK -> handleFacebookLogin(loginRequest);
            case APPLE -> handleAppleLogin(loginRequest);
            default -> throw new AppException(
                    StatusCode.UNSUPPORTED_PROVIDER,
                    BaseMessage.UNSUPPORTED_PROVIDER
            );
        };
    }

    private AuthResponse handleFacebookLogin(SocialLogin request) {
        try {
            // Verify token với Facebook Graph API
            FacebookClient facebookClient = new DefaultFacebookClient(
                    request.getAccessToken(),
                    Version.LATEST
            );

            com.restfb.types.User fbUser = facebookClient.fetchObject(
                    "me",
                    com.restfb.types.User.class,
                    Parameter.with("fields", "id,name,email,picture")
            );

            if (fbUser == null) {
                throw new AppException(StatusCode.UNAUTHORIZED, BaseMessage.INVALID_ID_TOKEN);
            }

            String email = fbUser.getEmail();
            String providerId = fbUser.getId();
            String name = fbUser.getName();
            String avatar = fbUser.getPicture() != null ? fbUser.getPicture().getUrl() : null;

            if (email == null) {
                email = "fb_" + providerId + "@facebook.com";
            }

            return createAndAuth(email, providerId, name, avatar, SocialType.FACEBOOK);

        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            throw new AppException(StatusCode.UNAUTHORIZED, BaseMessage.INVALID_ID_TOKEN);
        }
    }

    private AuthResponse handleAppleLogin(SocialLogin request) {
        try {
            // Decode JWT từ Apple (không cần verify signature ở đây
            // vì Firebase đã verify trước khi gửi lên BE)
            String[] parts = request.getIdToken().split("\\.");
            if (parts.length != 3) {
                throw new AppException(StatusCode.UNAUTHORIZED, BaseMessage.INVALID_ID_TOKEN);
            }

            // Decode payload
            String payload = new String(
                    java.util.Base64.getUrlDecoder().decode(parts[1])
            );

            // Parse JSON
            com.fasterxml.jackson.databind.ObjectMapper mapper =
                    new com.fasterxml.jackson.databind.ObjectMapper();
            com.fasterxml.jackson.databind.JsonNode node =
                    mapper.readTree(payload);

            String providerId = node.get("sub").asText();
            String email = node.has("email") ? node.get("email").asText() : null;

            // Apple không có name và avatar
            // Name chỉ có lần đầu tiên và do mobile gửi lên
            String name = request.getFullName();    // Thêm field này vào SocialLogin

            if (email == null) {
                email = "apple_" + providerId + "@privaterelay.appleid.com";
            }

            return createAndAuth(email, providerId, name, null, SocialType.APPLE);

        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            throw new AppException(StatusCode.UNAUTHORIZED, BaseMessage.INVALID_ID_TOKEN);
        }
    }

    private AuthResponse handleGoogleLogin(SocialLogin request) {

        GoogleIdToken.Payload payload = verifyGoogleToken(request.getIdToken());

        String email = payload.getEmail();
        String providerId = payload.getSubject();
        String name = (String) payload.get("name");
        String avatar = (String) payload.get("picture");

        return createAndAuth(email, providerId, name, avatar, SocialType.GOOGLE);
    }

    private AuthResponse createAndAuth(String email, String providerId,
                                       String name, String avatar,
                                       SocialType socialType) {
        String randomPassword = UUID.randomUUID().toString();
        Account account = accountRepository.findByEmail(email)
                .orElseGet(() -> {
                    Account newAccount = new Account();
                    newAccount.setEmail(email);
                    newAccount.setProvider(socialType);
                    newAccount.setUrlAvatar(avatar);
                    newAccount.setRole(AccountRole.CUSTOMER);
                    newAccount.setAccountTier(AccountTier.Free);
                    newAccount.setPassword(passwordEncoder.encode(randomPassword));
                    newAccount.setFullName("temp");
                    newAccount.setLoginWithBioMetric(false);
                    Account saved = accountRepository.save(newAccount);
                    saved.setFullName(name != null ? name : "Người dùng " + saved.getUserId());
                    return accountRepository.save(saved);
                });

        switch (socialType) {
            case GOOGLE -> account.setGoogleId(providerId);
            case FACEBOOK -> account.setFacebookId(providerId);
            case APPLE -> account.setAppleId(providerId);
        }
        accountRepository.save(account);

        String accessToken = jwtService.generateAccessToken(account.getEmail());
        String refreshToken = jwtService.generateRefreshToken(account.getEmail());
        saveRefreshToken(account, refreshToken);
        return buildAuthResponse(account, accessToken, refreshToken);
    }

    private GoogleIdToken.Payload verifyGoogleToken(String idTokenString) {

        NetHttpTransport transport = new NetHttpTransport();
        GsonFactory jsonFactory = GsonFactory.getDefaultInstance();

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier
                .Builder(new NetHttpTransport(), GsonFactory.getDefaultInstance())
                .setAudience(googleConfig.getClientIds())
                .build();

        try {

            GoogleIdToken idToken = verifier.verify(idTokenString);

            if (idToken == null) {
                throw new AppException(StatusCode.UNAUTHORIZED, BaseMessage.INVALID_ID_TOKEN);
            }

            return idToken.getPayload();

        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            throw new AppException(StatusCode.UNAUTHORIZED, BaseMessage.INVALID_ID_TOKEN);
        }
    }

    public AuthResponse refreshToken(RefreshTokenRequest request) {
        if (request == null) {
            throw new AppException(StatusCode.TOKEN_EXPIRED, BaseMessage.NOT_VALID_TOKEN);
        }
        Authentication authentication = authenticationRepository
                .findByRefreshToken(request.getRefreshToken())
                .orElseThrow(() -> new AppException(StatusCode.UNAUTHORIZED, BaseMessage.NOT_VALID_TOKEN));

        if (authentication.isRevoked()) {
            throw new AppException(StatusCode.UNAUTHORIZED, BaseMessage.TOKEN_RECALL);
        }

        if (authentication.getExpiryDate().isBefore(Instant.now())) {
            throw new AppException(StatusCode.TOKEN_EXPIRED, BaseMessage.TOKEN_EXPIRED);
        }

        String newAccessToken = jwtService.generateAccessToken(
                authentication.getAccount().getEmail()
        );

        return buildAuthResponse(
                authentication.getAccount(),
                newAccessToken,
                request.getRefreshToken()
        );
    }

    public AccountResponse getProfile(String email) {
        Account account = accountRepository.findByEmail(email).orElseThrow(()
                -> new AppException(StatusCode.EmailNotExists, BaseMessage.ACCOUNT_NOT_FOUND));
        return accountMapper.toResponse(account);
    }

    public void updateProfile(AccountUpdateRequest data, String email) {
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(StatusCode.NOT_FOUND, BaseMessage.ACCOUNT_NOT_FOUND));

        accountMapper.updateEntity(account, data);
        accountRepository.save(account);
        accountMapper.toResponse(account);
    }

    private void saveRefreshToken(Account account, String refreshToken) {
        Authentication authentication = authenticationRepository
                .findByAccount_UserId(account.getUserId())
                .orElse(new Authentication());
        authentication.setAccount(account);
        authentication.setRefreshToken(refreshToken);
        authentication.setExpiryDate(Instant.now().plusMillis(refreshTokenExpiration));
        authentication.setRevoked(false);
        authenticationRepository.save(authentication);
    }

    private AuthResponse buildAuthResponse(Account account, String accessToken, String refreshToken) {
        AuthResponse response = new AuthResponse();
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken);
        response.setUser_id(account.getUserId());
        response.setEmail(account.getEmail());
        response.setFullName(account.getFullName());
        response.setRole(account.getRole());
        response.setAccountTier(account.getAccountTier());
        return response;
    }
}
