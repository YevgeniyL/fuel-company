package com.fuelcompany.infrastructure.api.reporting;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class ApiFuelConsumptionRecord {
    private String fuelType;
    private BigDecimal volume;
    private BigDecimal averagePrice;
    private BigDecimal totalPrice;
}
