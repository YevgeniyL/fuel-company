package com.fuelcompany.domain.aggregateModels.report.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * list fuel consumption records for specified month (each row should contain: fuel type, volume, date, price, total price, driver ID)
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecordByMonthEntity {
    private Integer year;
    private String type;
    private BigDecimal volume;
    private LocalDate date;
    private BigDecimal price;
    private BigDecimal totalPrice;
    private Integer driverId;
}
