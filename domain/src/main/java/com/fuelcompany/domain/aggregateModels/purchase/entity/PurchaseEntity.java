package com.fuelcompany.domain.aggregateModels.purchase.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "PURCHASE", schema = "PUBLIC")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "FUEL_TYPE_ID", referencedColumnName = "ID")
    private FuelTypeEntity fuelType;

    @Column(name = "VOLUME", nullable = false, updatable = false)
    private BigDecimal volume;

    @Column(name = "PRICE", nullable = false, updatable = false)
    private BigDecimal price;

    @Column(name = "DRIVER_ID", nullable = false, updatable = false)
    private Long driverId;

    @Column(name = "DATE", nullable = false, updatable = false)
    private LocalDate date;

    @Column(name = "CREATED", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime created;

    public PurchaseEntity(FuelTypeEntity fuelType, BigDecimal volume, BigDecimal price, Long driverId, LocalDate date) {
        this.fuelType = fuelType;
        this.volume = volume;
        this.price = price;
        this.driverId = driverId;
        this.date = date;
    }
}
