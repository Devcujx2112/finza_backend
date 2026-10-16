package com.finza.backend.service;

import com.finza.backend.constant.BaseMessage;
import com.finza.backend.constant.StatusCode;
import com.finza.backend.dto.request.FixedCostRequest;
import com.finza.backend.dto.request.SetupSpendingConfigRequest;
import com.finza.backend.entity.Budget;
import com.finza.backend.entity.Categories;
import com.finza.backend.entity.ImagePath;
import com.finza.backend.entity.SpendingConfig;
import com.finza.backend.exception.AppException;
import com.finza.backend.repository.Account_repository;
import com.finza.backend.repository.CategoriesRepository;
import com.finza.backend.repository.FixedCostRepository;
import com.finza.backend.repository.ImagePathRepository;
import com.finza.backend.repository.SpendingConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class OnboardingSetupService {
    private final FixedCostRepository fixedCostRepository;
    private final SpendingConfigRepository spendingConfigRepository;
    private final Account_repository accountRepository;
    private final CategoriesRepository categoriesRepository;

    @Transactional
    public void saveDataSpendingConfig(SetupSpendingConfigRequest data) {
        validateSpendingConfig(data);
        Long userId = getCurrentUserId();

        SpendingConfig spendingConfig = spendingConfigRepository
                .findByUserId(userId)
                .orElseGet(() -> {
                    SpendingConfig created = new SpendingConfig();
                    created.setUserId(userId);
                    return created;
                });

        boolean isLastDayOfMonth = Boolean.TRUE.equals(data.getIsLastDayOfMonth());
        spendingConfig.setSpendingMethod(data.getSpendingMethod());
        spendingConfig.setIsLastDayOfMonth(isLastDayOfMonth);
        spendingConfig.setDayStarted(isLastDayOfMonth ? 0 : data.getDayStarted());
        spendingConfig.setTotalIncome(data.getTotalInCome());
        spendingConfig.setMonthlySaving(data.getMonthlySaving());

        spendingConfigRepository.save(spendingConfig);
    }

    private void validateSpendingConfig(SetupSpendingConfigRequest data) {

        if (data == null) {
            throw new AppException(StatusCode.BAD_REQUEST, BaseMessage.REQUEST_NULL);
        }

        if (data.getSpendingMethod() == null) {
            throw new AppException(StatusCode.BAD_REQUEST, BaseMessage.SPENDING_METHOD_NULL);
        }

        if (!Boolean.TRUE.equals(data.getIsLastDayOfMonth())) {
            Integer dayStarted = data.getDayStarted();

            if (dayStarted == null || dayStarted < 1 || dayStarted > 31) {
                throw new AppException(StatusCode.BAD_REQUEST, BaseMessage.DAY_STARTED_INVALID);
            }
        }

        if (data.getTotalInCome() == null || data.getTotalInCome() < 0) {
            throw new AppException(StatusCode.BAD_REQUEST, BaseMessage.TOTAL_INCOME_INVALID);
        }

        Long monthlySaving = data.getMonthlySaving();

        if (monthlySaving != null) {
            if (monthlySaving < 0) {
                throw new AppException(StatusCode.BAD_REQUEST, BaseMessage.MONTHLY_SAVING_INVALID);
            }

            if (monthlySaving > data.getTotalInCome()) {
                throw new AppException(
                        StatusCode.BAD_REQUEST, BaseMessage.MONTHLY_SAVING_OVER_INCOME);
            }
        }
    }

    @Transactional
    public void saveBugetUser(List<FixedCostRequest> request){
        Long userId = getCurrentUserId();

        if (request == null || request.isEmpty()) {
            return;
        }

        Long spendingConfigId = spendingConfigRepository
                .findByUserId(userId)
                .orElseThrow(() -> new AppException(
                        StatusCode.NOT_FOUND, BaseMessage.SPENDING_CONFIG_NOT_FOUND))
                .getSpendingConfigId();

        for (FixedCostRequest item : request) {

            Categories categories = new Categories();
            categories.setUserId(userId);
            categories.setCategoriesName(item.getCategoriesName());
            categories.setStringIcon(item.getStringIcon());

            Long categoriesId = categoriesRepository.save(categories).getCategoriesId();

            Budget budget = new Budget();
            budget.setSpendingConfigId(spendingConfigId);
            budget.setTypeCost(item.getTypeCost());
            budget.setNameCost(item.getNameCost());
            budget.setAmount(item.getAmount());
            budget.setCategoriesId(categoriesId);

            fixedCostRepository.save(budget);
        }
    }

    private Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            throw new AppException(StatusCode.UNAUTHORIZED, BaseMessage.UNAUTHORIZED);
        }

        String email = auth.getName();

        return accountRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(
                        StatusCode.NOT_FOUND, BaseMessage.ACCOUNT_NOT_FOUND))
                .getUserId();
    }
}
