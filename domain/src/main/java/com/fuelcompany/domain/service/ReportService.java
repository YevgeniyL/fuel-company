package com.fuelcompany.domain.service;

import com.fuelcompany.domain.aggregateModels.report.FuelConsumption;
import com.fuelcompany.domain.aggregateModels.report.Month;
import com.fuelcompany.domain.aggregateModels.report.entity.TotalByMonthEntity;
import com.fuelcompany.domain.errors.DomainException;

import java.util.List;

public interface ReportService {

    List<TotalByMonthEntity> getAmountByMonths(Long driverId) throws DomainException;

    List<Month> getReportByMonth(int numberOfMonth, Long driverId, Integer year) throws DomainException;

    List<FuelConsumption> getFuelConsumption(Long driverId, Integer year) throws DomainException;
}
