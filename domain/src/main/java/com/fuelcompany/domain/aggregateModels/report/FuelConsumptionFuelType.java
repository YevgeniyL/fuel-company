package com.fuelcompany.domain.aggregateModels.report;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FuelConsumptionFuelType {
    private String fuelType;
    private BigDecimal volume;
    private BigDecimal averagePrice;
    private BigDecimal totalPrice;
}
