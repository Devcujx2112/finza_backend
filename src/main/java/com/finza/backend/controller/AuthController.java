package com.finza.backend.controller;

import com.finza.backend.constant.BaseMessage;
import com.finza.backend.constant.StatusCode;
import com.finza.backend.dto.request.*;
import com.finza.backend.dto.response.AccountResponse;
import com.finza.backend.dto.response.AuthResponse;
import com.finza.backend.dto.response.BaseParam;
import com.finza.backend.dto.response.BaseResponse;
import com.finza.backend.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<BaseResponse<AccountResponse>> register(@Valid @RequestBody AccountRequest request) {
        AccountResponse data = authService.register(request);
        return ResponseEntity.ok(
                BaseResponse.success(new BaseParam<>(data, StatusCode.SUCCESS, BaseMessage.REGISTER_SUCCESS))
        );
    }

    @PostMapping("/login-social")
    public ResponseEntity<BaseResponse<AuthResponse>> loginSocial(@Valid @RequestBody SocialLogin request) {
        AuthResponse data = authService.loginWithSocial(request);
        return ResponseEntity.ok(
                BaseResponse.success(new BaseParam<>(data, StatusCode.SUCCESS, BaseMessage.LOGIN_SOCIAL_SUCCESS))
        );
    }

    @PostMapping("/trial")
    public ResponseEntity<BaseResponse<AuthResponse>> registerTrial() {
        AuthResponse data = authService.registerTrial();
        return ResponseEntity.ok(
                BaseResponse.success(new BaseParam<>(data, StatusCode.SUCCESS, BaseMessage.REGISTER_SUCCESS))
        );
    }

    @PostMapping("/login")
    public ResponseEntity<BaseResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse data = authService.login(request);
        return ResponseEntity.ok(
                BaseResponse.success(new BaseParam<>(data, StatusCode.SUCCESS, BaseMessage.LOGIN_SUCCESS))
        );
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<BaseResponse<AuthResponse>> refreshToken(
            @Valid @RequestBody RefreshTokenRequest request) {
        AuthResponse data = authService.refreshToken(request);
        return ResponseEntity.ok(
                BaseResponse.success(new BaseParam<>(data, StatusCode.SUCCESS, BaseMessage.LOGIN_SUCCESS))
        );
    }

    @PostMapping("/send-otp")
    public ResponseEntity<BaseResponse<Void>> sendOtp(@RequestBody ForgotPasswordRequest request) {
        authService.forgotPassword(request.getEmail());
        return ResponseEntity.ok(
                BaseResponse.successNullData(new BaseParam<>(null, StatusCode.SUCCESS, BaseMessage.SendOtpSuccess))
        );
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<BaseResponse<Void>> verifyOtp(@RequestBody VerifyOTP request) {
        authService.verifyOtp(request);
        return ResponseEntity.ok(
                BaseResponse.successNullData(new BaseParam<>(null, StatusCode.SUCCESS, BaseMessage.VERIFY_OTP_SUCCESS))
        );
    }

    @PostMapping("/change-password")
    public ResponseEntity<BaseResponse<Void>> changePassword(@RequestBody LoginRequest request) {
        authService.changePassword(request);
        return ResponseEntity.ok(BaseResponse.successNullData(new BaseParam<>(null, StatusCode.SUCCESS, BaseMessage.ChangePasswordSuccess)));
    }
}
