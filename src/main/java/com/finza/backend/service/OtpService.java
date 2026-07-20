package com.finza.backend.service;

import com.finza.backend.constant.BaseMessage;
import com.finza.backend.constant.StatusCode;
import com.finza.backend.exception.AppException;
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

    public void verifyOtp(String email, String otp) {
        String cacheOtp = otpRedisService.getOtp(email);
        if (cacheOtp == null) {
            throw new AppException(StatusCode.OTP_EXPIRED, BaseMessage.OTP_EXPIRED);
        }
        if (!cacheOtp.equals(otp)) {
            throw new AppException(StatusCode.OTP_INVALID, BaseMessage.OTP_INVALID);
        }
        otpRedisService.deleteOtp(email);
    }
}
