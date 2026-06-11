package com.finza.backend.dto.request;

import com.finza.backend.constant.BaseMessage;
import com.finza.backend.constant.StatusCode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AccountRequest {
    private String email;

    private String fullName;

    private String phoneNumber;

    private String password;

    private String dateOfBirth;
}