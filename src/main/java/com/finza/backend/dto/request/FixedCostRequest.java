package com.finza.backend.dto.request;
import com.finza.backend.entity.TypeCost;
import lombok.Data;

@Data
public class FixedCostRequest {
    private TypeCost typeCost;

    private String nameCost;

    private Long amount;

    private String categoriesName;

    private String stringIcon;
}
