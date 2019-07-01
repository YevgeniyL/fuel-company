package com.fuelcompany.infrastructure.api.reporting;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * list fuel consumption records for specified month (each row should contain: fuel type, volume, date, price, total price, driver ID)
 */

@Getter
@AllArgsConstructor
public class ApiReportByMonthRecord {
    private String type;
    private BigDecimal volume;
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate date;
    private BigDecimal price;
    private BigDecimal totalPrice;
    private Integer driverId;
}
