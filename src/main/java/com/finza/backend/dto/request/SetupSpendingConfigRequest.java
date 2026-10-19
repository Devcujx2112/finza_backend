package com.finza.backend.dto.request;

import com.finza.backend.entity.SpendingMethod;
import lombok.Data;

@Data
public class SetupSpendingConfigRequest {
    private SpendingMethod spendingMethod;

    private Integer dayStarted;

    private Boolean isLastDayOfMonth;

    private Long totalInCome;

    private Long monthlySaving;
}
