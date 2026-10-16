package com.finza.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "spending_config")
@Data
public class SpendingConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Long spendingConfigId;

    @Column(unique = true)
    private Long userId;

    @Enumerated(EnumType.STRING)
    private SpendingMethod spendingMethod;

    private int dayStarted;

    private Boolean isLastDayOfMonth;

    private Long totalIncome;

    private Long monthlySaving;
}
