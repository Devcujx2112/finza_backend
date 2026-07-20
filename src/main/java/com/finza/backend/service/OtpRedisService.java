package com.finza.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class OtpRedisService {

    private final StringRedisTemplate redisTemplate;

    public void saveOtp(String email, String otp) {
        redisTemplate.opsForValue().set(
                "otp:forgot:" + email,
                otp,
                Duration.ofMinutes(3)
        );
    }

    public String getOtp(String email) {
        return redisTemplate.opsForValue().get("otp:forgot:" + email);
    }

    public void deleteOtp(String email) {
        redisTemplate.delete("otp:forgot:" + email);
    }
}