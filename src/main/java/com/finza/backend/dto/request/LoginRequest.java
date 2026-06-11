package com.finza.backend.dto.request;

import com.finza.backend.constant.BaseMessage;
import com.finza.backend.constant.StatusCode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}
