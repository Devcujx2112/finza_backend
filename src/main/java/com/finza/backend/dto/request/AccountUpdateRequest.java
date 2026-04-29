package com.finza.backend.dto.request;

import lombok.Data;

@Data
public class AccountUpdateRequest {
    private String fullName;
    private String phoneNumber;
    private String dateOfBirth;
    private String urlAvatar;
}
