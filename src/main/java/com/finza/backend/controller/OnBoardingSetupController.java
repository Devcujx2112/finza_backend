package com.finza.backend.controller;

import com.finza.backend.constant.BaseMessage;
import com.finza.backend.constant.StatusCode;
import com.finza.backend.dto.request.FixedCostRequest;
import com.finza.backend.dto.request.SetupSpendingConfigRequest;
import com.finza.backend.dto.response.BaseParam;
import com.finza.backend.dto.response.BaseResponse;
import com.finza.backend.service.OnboardingSetupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/onboarding")
@RequiredArgsConstructor
public class OnBoardingSetupController {
    private final OnboardingSetupService onboardingSetupService;

    @PostMapping("/save-spendingConfig")
    public ResponseEntity<BaseResponse<Void>> setupSpendingConfig(@Valid @RequestBody SetupSpendingConfigRequest request) {
        onboardingSetupService.saveDataSpendingConfig(request);
        return ResponseEntity.ok(
                BaseResponse.successNullData(new BaseParam<>(null, StatusCode.SUCCESS, BaseMessage.SETUP_SPENDING_CONFIG_SUCCESS))
        );
    }

    @PostMapping("/save-budget")
    public ResponseEntity<BaseResponse<Void>> setupBudgetUser(@Valid @RequestBody List<FixedCostRequest> request) {
        onboardingSetupService.saveBugetUser(request);
        return ResponseEntity.ok(
                BaseResponse.successNullData(new BaseParam<>(null, StatusCode.SUCCESS, BaseMessage.SETUP_SPENDING_CONFIG_SUCCESS))
        );
    }
}
