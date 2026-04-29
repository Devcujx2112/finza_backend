package com.finza.backend.mapper;

import com.finza.backend.dto.request.AccountRequest;
import com.finza.backend.dto.response.AccountResponse;
import com.finza.backend.dto.request.AccountUpdateRequest;
import com.finza.backend.entity.Account;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {

    // Register
    public Account toEntity(AccountRequest request) {
        Account account = new Account();
        account.setUserName(request.getUserName());
        account.setFullName(request.getFullName());
        account.setPhoneNumber(request.getPhoneNumber());
        account.setPassword(request.getPassword());
        account.setDateOfBirth(request.getDateOfBirth());
        return account;
    }

    // Tra ve data cho cliend
    public AccountResponse toResponse(Account account) {
        AccountResponse response = new AccountResponse();
        response.setUser_id(account.getUserId());
        response.setUserName(account.getUserName());
        response.setFullName(account.getFullName());
        response.setPhoneNumber(account.getPhoneNumber());
        response.setDateOfBirth(account.getDateOfBirth());
        response.setUrlAvatar(account.getUrlAvatar());
        response.setAccountTier(account.getAccountTier());
        response.setRole(account.getRole());
        return response;
    }

    //Update profile
    public void updateEntity(Account account, AccountUpdateRequest request) {
        account.setFullName(request.getFullName());
        account.setPhoneNumber(request.getPhoneNumber());
        account.setDateOfBirth(request.getDateOfBirth());
        account.setUrlAvatar(request.getUrlAvatar());
        // KHÔNG set userName, role, accountTier!
    }
}
