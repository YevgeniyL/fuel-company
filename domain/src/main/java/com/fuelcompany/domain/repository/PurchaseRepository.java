package com.fuelcompany.domain.repository;

import com.fuelcompany.domain.aggregateModels.purchase.entity.PurchaseEntity;
import com.fuelcompany.domain.aggregateModels.report.entity.FuelConsumptionEntity;
import com.fuelcompany.domain.aggregateModels.report.entity.RecordByMonthEntity;
import com.fuelcompany.domain.aggregateModels.report.entity.TotalByMonthEntity;

import java.util.List;

public interface PurchaseRepository{

    PurchaseEntity save(PurchaseEntity report);

    List<TotalByMonthEntity> getAmountByMonths(Long driverId);

    List<RecordByMonthEntity> getReportByMonth(int monthsNumber, Long driverId, Integer year);

    List<FuelConsumptionEntity> getFuelConsumption(Long driverId, Integer year);
}
