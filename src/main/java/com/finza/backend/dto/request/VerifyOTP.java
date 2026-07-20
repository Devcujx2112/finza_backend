package com.finza.backend.dto.request;

import lombok.Data;

@Data
public class VerifyOTP {
    private String email;
    private String otp;
}
