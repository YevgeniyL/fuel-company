package com.fuelcompany.infrastructure.api.reporting;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * statistics for each month, list fuel consumption records grouped by fuel type (each row should contain: fuel type, volume, average price, total price)
 */
@Getter
@AllArgsConstructor
public class ApiFuelConsumption {
    private Integer year;
    private String month;
    private List<ApiFuelConsumptionRecord> fuelTypes;
}
