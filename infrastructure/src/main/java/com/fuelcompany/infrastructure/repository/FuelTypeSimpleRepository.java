package com.fuelcompany.infrastructure.repository;

import com.fuelcompany.domain.aggregateModels.purchase.entity.FuelTypeEntity;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface FuelTypeSimpleRepository extends SimpleRepository<FuelTypeEntity, Long> {

    Optional<FuelTypeEntity> findByNameAndDisabledIsNull(String name);

    List<FuelTypeEntity> findAllByNameInAndDisabledIsNull(Set<String> fuelTypeNames);

}