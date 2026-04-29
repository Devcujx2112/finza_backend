package com.finza.backend.controller;


import com.finza.backend.constant.BaseMessage;
import com.finza.backend.constant.StatusCode;
import com.finza.backend.dto.request.AccountRequest;
import com.finza.backend.dto.request.LoginRequest;
import com.finza.backend.dto.response.AccountResponse;
import com.finza.backend.dto.response.AuthResponse;
import com.finza.backend.dto.response.BaseParam;
import com.finza.backend.dto.response.BaseResponse;
import com.finza.backend.service.AccountService;
import com.finza.backend.service.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<BaseResponse<AccountResponse>> register(@Valid @RequestBody AccountRequest request){
        AccountResponse data = accountService.register(request);
        return ResponseEntity.ok(
                BaseResponse.success(new BaseParam<>(data, StatusCode.SUCCESS, BaseMessage.REGISTER_SUCCESS))
        );
    }

    @PostMapping("/login")
    public ResponseEntity<BaseResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request){
        AuthResponse data = accountService.login(request);
        return ResponseEntity.ok(
                BaseResponse.success(new BaseParam<>(data, StatusCode.SUCCESS, BaseMessage.LOGIN_SUCCESS))
        );
    }

    @GetMapping("/getProfile")
    public ResponseEntity<BaseResponse<AccountResponse>> getProfile(@Valid @RequestHeader("Authorization") String token){
        String userName = jwtService.extractUserName(token.replace("Bearer ", ""));
        AccountResponse data = accountService.getProfile(userName);
        return ResponseEntity.ok(
                BaseResponse.success(new BaseParam<>(data, StatusCode.SUCCESS, BaseMessage.GET_PROFILE_SUCCESS)));
    }
}
