package com.fuelcompany.infrastructure.api.registration;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ApiPurchase {
    private Long id;
    private BigDecimal volume;
    private String fuelType;
    private BigDecimal price;
    private Long driverId;
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate date;

    public ApiPurchase(String fuelType, BigDecimal volume, BigDecimal price, Long driverId, LocalDate date) {
        this.fuelType = fuelType;
        this.volume = volume;
        this.price = price;
        this.driverId = driverId;
        this.date = date;
    }
}