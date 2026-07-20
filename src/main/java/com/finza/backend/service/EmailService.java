package com.finza.backend.service;

import com.finza.backend.dto.request.ForgotPasswordRequest;

public interface EmailService {
    void sendOtp(String email, String otp);
}
