package com.finza.backend.dto.request;

import lombok.Data;

@Data
public class ForgotPasswordRequest {
    private String email;
    private String expiredTime;
    private String pinString;
}
