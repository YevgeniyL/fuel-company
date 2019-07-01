package com.fuelcompany.infrastructure.api.reporting;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * list fuel consumption records for specified month (each row should contain: fuel type, volume, date, price, total price, driver ID)
 */

@Getter
@AllArgsConstructor
public class ApiReportByMonth {
    private Integer year;
    private List<ApiReportByMonthRecord> records;
}
