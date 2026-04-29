package com.finza.backend.dto.request;

import com.finza.backend.constant.BaseMessage;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RefreshTokenRequest {
    @NotBlank(message = BaseMessage.NOT_NULL_ACCESSTOKEN)
    private String refreshToken;
}