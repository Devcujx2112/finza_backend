package com.finza.backend.dto.response;

import com.finza.backend.entity.AccountRole;
import com.finza.backend.entity.AccountTier;
import lombok.Data;

@Data
public class AccountResponse {
    private Long user_id;
    private String userName;
    private String fullName;
    private String phoneNumber;
    private String dateOfBirth;
    private String urlAvatar;
    private AccountTier accountTier;
    private AccountRole role;
}
