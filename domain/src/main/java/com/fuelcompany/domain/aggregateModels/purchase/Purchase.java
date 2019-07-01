package com.fuelcompany.domain.aggregateModels.purchase;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Purchase {
    private Long id;
    private BigDecimal volume;
    private String fuelType;
    private BigDecimal price;
    private Long driverId;
    private LocalDate date;

    public Purchase(String fuelType, BigDecimal volume, BigDecimal price, Long driverId, LocalDate date) {
        this.volume = volume;
        this.fuelType = fuelType;
        this.price = price;
        this.driverId = driverId;
        this.date = date;
    }
}
