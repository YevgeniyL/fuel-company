package com.fuelcompany.domain.repository;

import com.fuelcompany.domain.aggregateModels.purchase.entity.FuelTypeEntity;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface FuelTypeRepository {

    Optional<FuelTypeEntity> findActiveByName(String fuelType);

    List<FuelTypeEntity> findActiveByName(Set<String> fuelTypeNames);
}
