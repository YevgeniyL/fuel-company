package com.fuelcompany.infrastructure.api.reporting;

import com.fuelcompany.domain.aggregateModels.report.FuelConsumption;
import com.fuelcompany.domain.aggregateModels.report.FuelConsumptionFuelType;
import com.fuelcompany.domain.aggregateModels.report.Month;
import com.fuelcompany.domain.aggregateModels.report.RecordByMonthItem;
import com.fuelcompany.domain.aggregateModels.report.entity.TotalByMonthEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ReportTransformer {

    public List<ApiTotalGroupByMonth> toRESTAmountMoney(List<TotalByMonthEntity> total) {
        return total.stream().map(this::toRESTAmountMoney).collect(Collectors.toList());
    }

    private ApiTotalGroupByMonth toRESTAmountMoney(TotalByMonthEntity item) {
        return new ApiTotalGroupByMonth(item.getYear(), item.getMonth(), roundTo2AndStripZeros(item.getTotal()));
    }

    public List<ApiReportByMonth> toRESTByMonth(List<Month> totalInMonth) {
        if (totalInMonth == null) return Collections.emptyList();
        return totalInMonth.stream()
                .map(month ->
                        new ApiReportByMonth(month.getYear(), collectRecordsInMonth(month.getRecords())))
                .collect(Collectors.toList());
    }

    private List<ApiReportByMonthRecord> collectRecordsInMonth(List<RecordByMonthItem> months) {
        if (months == null) return Collections.emptyList();
        return months.stream()
                .map(r ->
                        new ApiReportByMonthRecord(r.getType(), roundTo3AndStripZeros(r.getVolume()), r.getDate(), roundTo2AndStripZeros(r.getPrice()), roundTo2AndStripZeros(r.getTotalPrice()), r.getDriverId()))
                .collect(Collectors.toList());
    }

    public List<ApiFuelConsumption> toRESTFuelConsumption(List<FuelConsumption> fuelConsumption) {
        if (fuelConsumption == null) return Collections.emptyList();
        return fuelConsumption.stream()
                .map(f ->
                        new ApiFuelConsumption(f.getYear(), f.getMonth(), getFuelConsumptions(f.getFuelTypes())))
                .collect(Collectors.toList());
    }

    private List<ApiFuelConsumptionRecord> getFuelConsumptions(List<FuelConsumptionFuelType> list) {
        return list.stream()
                .map(r ->
                        new ApiFuelConsumptionRecord(r.getFuelType(), roundTo3AndStripZeros(r.getVolume()), roundTo2AndStripZeros(r.getAveragePrice()), roundTo3AndStripZeros(r.getTotalPrice())))
                .collect(Collectors.toList());
    }

    private BigDecimal roundTo2AndStripZeros(BigDecimal unRounded) {
        return unRounded.setScale(2, RoundingMode.HALF_EVEN);
    }

    private BigDecimal roundTo3AndStripZeros(BigDecimal unRounded) {
        return unRounded.setScale(3, RoundingMode.HALF_EVEN);
    }
}
