package com.finza.backend.dto.request;

import com.finza.backend.constant.BaseMessage;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RefreshTokenRequest {
    private String refreshToken;
}