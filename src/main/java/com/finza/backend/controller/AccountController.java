package com.finza.backend.controller;


import com.finza.backend.constant.BaseMessage;
import com.finza.backend.constant.StatusCode;
import com.finza.backend.dto.request.*;
import com.finza.backend.dto.response.AccountResponse;
import com.finza.backend.dto.response.AuthResponse;
import com.finza.backend.dto.response.BaseParam;
import com.finza.backend.dto.response.BaseResponse;
import com.finza.backend.service.AccountService;
import com.finza.backend.service.AuthService;
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

    @GetMapping("/getProfile")
    public ResponseEntity<BaseResponse<AccountResponse>> getProfile(@Valid @RequestHeader("Authorization") String token) {
        String email = jwtService.extractEmail(token.replace("Bearer ", ""));
        AccountResponse data = accountService.getProfile(email);
        return ResponseEntity.ok(
                BaseResponse.success(new BaseParam<>(data, StatusCode.SUCCESS, BaseMessage.GET_PROFILE_SUCCESS)));
    }

    @PutMapping("/updateProfile")
    public ResponseEntity<BaseResponse<AccountResponse>> updateProfile(@RequestBody AccountUpdateRequest data, @RequestHeader("Authorization") String token) {
        String email = jwtService.extractEmail(token.replace("Bearer ", ""));
        accountService.updateProfile(data, email);
        return ResponseEntity.ok(BaseResponse.successNullData(new BaseParam<>(null, StatusCode.SUCCESS, BaseMessage.SUCCESS)));
    }
}
