package com.finza.backend.dto.request;

import lombok.Data;

@Data
public class AccountRequest {
    private String userName;
    private String fullName;
    private String phoneNumber;
    private String password;
    private String dateOfBirth;
}
