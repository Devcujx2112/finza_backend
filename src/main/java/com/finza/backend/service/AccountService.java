package com.finza.backend.service;
import com.finza.backend.constant.BaseMessage;
import com.finza.backend.constant.StatusCode;
import com.finza.backend.dto.request.*;
import com.finza.backend.dto.response.AccountResponse;
import com.finza.backend.entity.*;
import com.finza.backend.exception.AppException;
import com.finza.backend.mapper.AccountMapper;
import com.finza.backend.repository.Account_repository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final Account_repository accountRepository;
    private final AccountMapper accountMapper;

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

//    private AuthResponse buildAuthResponse(Account account, String accessToken, String refreshToken) {
//        AuthResponse response = new AuthResponse();
//        response.setAccessToken(accessToken);
//        response.setRefreshToken(refreshToken);
//        response.setUser_id(account.getUserId());
//        response.setEmail(account.getEmail());
//        response.setFullName(account.getFullName());
//        response.setRole(account.getRole());
//        response.setAccountTier(account.getAccountTier());
//        return response;
//    }

//    private void saveRefreshToken(Account account, String refreshToken) {
//        Authentication authentication = authenticationRepository
//                .findByAccount_UserId(account.getUserId())
//                .orElse(new Authentication());
//        authentication.setAccount(account);
//        authentication.setRefreshToken(refreshToken);
//        authentication.setExpiryDate(Instant.now().plusMillis(refreshTokenExpiration));
//        authentication.setRevoked(false);
//        authenticationRepository.save(authentication);
//    }
}
