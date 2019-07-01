package com.fuelcompany.infrastructure.repository;

import com.fuelcompany.domain.aggregateModels.purchase.entity.FuelTypeEntity;
import com.fuelcompany.domain.repository.FuelTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class FuelTypeRepositoryImpl implements FuelTypeRepository {

    @Autowired
    private FuelTypeSimpleRepository simpleRepository;

    @Override
    @Transactional(readOnly = true)
    public Optional<FuelTypeEntity> findActiveByName(String name) {
        return simpleRepository.findByNameAndDisabledIsNull(name);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FuelTypeEntity> findActiveByName(Set<String> fuelTypeNames) {
        return simpleRepository.findAllByNameInAndDisabledIsNull(fuelTypeNames);
    }
}
