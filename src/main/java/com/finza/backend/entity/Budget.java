package com.finza.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Budget")
@Data
public class Budget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Long budgetId;

    private Long spendingConfigId;

    @Enumerated(EnumType.STRING)
    private TypeCost typeCost;

    private String nameCost;

    private Long amount;

    private Long categoriesId;
}
