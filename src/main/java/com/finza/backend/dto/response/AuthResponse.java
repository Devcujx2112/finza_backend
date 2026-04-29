package com.finza.backend.dto.response;

import com.finza.backend.entity.AccountRole;
import com.finza.backend.entity.AccountTier;
import lombok.Data;

@Data
public class AuthResponse {
    private String accessToken;
    private String refreshToken;
    private Long user_id;
    private String userName;
    private String fullName;
    private AccountRole role;
    private AccountTier accountTier;
}
