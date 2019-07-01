package com.fuelcompany.domain.aggregateModels.report.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * statistics for each month, list fuel consumption records grouped by fuel type
 * (each row should contain: fuel type, volume, average price, total price)
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FuelConsumptionEntity {
    private String type;
    private BigDecimal volume;
    private BigDecimal averagePrice;
    private BigDecimal totalPrice;
    private int year;
    private String month;
}
