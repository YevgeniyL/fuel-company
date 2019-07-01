package com.fuelcompany.domain.aggregateModels.report;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class RecordByMonthItem {
    private String type;
    private BigDecimal volume;
    private LocalDate date;
    private BigDecimal price;
    private BigDecimal totalPrice;
    private Integer driverId;
}
