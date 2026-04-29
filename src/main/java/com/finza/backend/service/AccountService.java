package com.finza.backend.service;

import com.finza.backend.constant.BaseMessage;
import com.finza.backend.dto.request.AccountRequest;
import com.finza.backend.dto.request.LoginRequest;
import com.finza.backend.dto.request.RefreshTokenRequest;
import com.finza.backend.dto.response.AccountResponse;
import com.finza.backend.dto.response.AuthResponse;
import com.finza.backend.entity.Account;
import com.finza.backend.entity.AccountRole;
import com.finza.backend.entity.AccountTier;
import com.finza.backend.entity.Authentication;
import com.finza.backend.mapper.AccountMapper;
import com.finza.backend.repository.Account_repository;
import com.finza.backend.repository.Authentication_repository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final Account_repository accountRepository;
    private final Authentication_repository authenticationRepository;
    private final AccountMapper accountMapper;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AccountResponse register(AccountRequest request) {
        if (accountRepository.existsByUserName(request.getUserName())) {
            throw new RuntimeException(BaseMessage.USERNAME_EXISTED);
        }

        Account account = accountMapper.toEntity(request);
        account.setPassword(passwordEncoder.encode((request.getPassword())));
        account.setRole(AccountRole.CUSTOMER);
        account.setAccountTier(AccountTier.Free);

        accountRepository.save(account);
        return accountMapper.toResponse(account);
    }

    public AuthResponse login(LoginRequest loginRequest) {
        Account account = accountRepository.findByUserName(loginRequest.getUserName()).orElseThrow(() ->
                new RuntimeException(BaseMessage.ACCOUNT_NOT_FOUND));
        if (!passwordEncoder.matches(loginRequest.getPassword(), account.getPassword())) {
            throw new RuntimeException(BaseMessage.WRONG_PASSWORD);
        }

        String accessToken = jwtService.generateAccessToken(account.getUserName());
        String refreshToken = jwtService.generateRefreshToken(account.getUserName());

        Authentication authentication = authenticationRepository.findByAccount_UserId(account.getUserId()).
                orElse(new Authentication());

        authentication.setAccount(account);
        authentication.setRefreshToken(refreshToken);
        authentication.setExpiryDate(Instant.now().plusMillis(604800000));
        authentication.setRevoked(false);
        authenticationRepository.save(authentication);

        return buildAuthResponse(account, accessToken, refreshToken);
    }

    public AuthResponse refreshToken(RefreshTokenRequest request) {
        Authentication authentication = authenticationRepository
                .findByRefreshToken(request.getRefreshToken())
                .orElseThrow(() -> new RuntimeException(BaseMessage.NOT_VALID_TOKEN));

        // 2. Kiểm tra token đã bị thu hồi chưa
        if (authentication.isRevoked()) {
            throw new RuntimeException(BaseMessage.TOKEN_RECALL);
        }

        // 3. Kiểm tra token còn hạn không
        if (authentication.getExpiryDate().isBefore(Instant.now())) {
            throw new RuntimeException(BaseMessage.TOKEN_EXPIRED);
        }

        // 4. Generate Access Token mới
        String newAccessToken = jwtService.generateAccessToken(
                authentication.getAccount().getUserName()
        );

        return buildAuthResponse(
                authentication.getAccount(),
                newAccessToken,
                request.getRefreshToken()
        );
    }

    private AuthResponse buildAuthResponse(Account account, String accessToken, String refreshToken) {
        AuthResponse response = new AuthResponse();
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken);
        response.setUser_id(account.getUserId());
        response.setUserName(account.getUserName());
        response.setFullName(account.getFullName());
        response.setRole(account.getRole());
        response.setAccountTier(account.getAccountTier());
        return response;
    }

    public AccountResponse getProfile(String userName) {
        Account account = accountRepository.findByUserName(userName).orElseThrow(()
                -> new RuntimeException(BaseMessage.ACCOUNT_NOT_FOUND));
        return accountMapper.toResponse(account);
    }
}
