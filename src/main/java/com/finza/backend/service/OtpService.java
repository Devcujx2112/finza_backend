package com.finza.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class OtpService {
    private static final SecureRandom RANDOM = new SecureRandom();

    private final OtpRedisService otpRedisService;
    private final EmailService emailService;

    public void sendForgotPasswordOtp(String email) {
        String otp = generateOtp();
        otpRedisService.saveOtp(email, otp);
        emailService.sendOtp(email, otp);
    }

    private String generateOtp() {
        return String.format("%06d", RANDOM.nextInt(1_000_000));
    }

//    public boolean verifyOtp(String email, String otp) {
//    }
//
//    public void resendOtp(String email) {
//    }
}
