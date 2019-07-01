package com.fuelcompany.domain.aggregateModels.report.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TotalByMonthEntity {
    private int year;
    @Setter
    private String month;
    private BigDecimal total;
}
