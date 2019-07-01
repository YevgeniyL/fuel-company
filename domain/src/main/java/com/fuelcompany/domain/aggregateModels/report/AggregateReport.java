package com.fuelcompany.domain.aggregateModels.report;

import com.fuelcompany.domain.aggregateModels.report.entity.FuelConsumptionEntity;
import com.fuelcompany.domain.aggregateModels.report.entity.RecordByMonthEntity;
import com.fuelcompany.domain.aggregateModels.report.entity.TotalByMonthEntity;
import com.fuelcompany.domain.errors.DomainException;
import com.fuelcompany.domain.errors.ErrorMessages;
import com.fuelcompany.domain.repository.PurchaseRepository;
import com.fuelcompany.domain.service.ReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AggregateReport implements ReportService {
    private static Logger logger = LoggerFactory.getLogger(AggregateReport.class);

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Override
    public List<TotalByMonthEntity> getAmountByMonths(Long driverId) throws DomainException {
        try {
            final List<TotalByMonthEntity> totalByMonth = purchaseRepository.getAmountByMonths(driverId);
            for (TotalByMonthEntity item : totalByMonth) {
                item.setMonth(getMonthName(item.getMonth()));
            }
            return totalByMonth;
        } catch (Exception e) {
            logger.error("Saving process error", e);
            throw new DomainException(ErrorMessages.DOMAIN_ERROR_E9999);
        }
    }

    private String getMonthName(String number) {
        return java.time.Month.of(Integer.valueOf(number)).name();
    }

    @Override
    public List<Month> getReportByMonth(int numberOfMonth, Long driverId, Integer year) throws DomainException {
        try {
            final List<RecordByMonthEntity> reportByMonth = purchaseRepository.getReportByMonth(numberOfMonth, driverId, year);
            final Map<Integer, List<RecordByMonthEntity>> yearsMap = reportByMonth.stream().collect(Collectors.groupingBy(RecordByMonthEntity::getYear));
            return yearsMap.keySet().stream()
                    .map(yearKey ->
                            new Month(yearKey, collectAllMonthsRecords(yearsMap.get(yearKey))))
                    .sorted((o1, o2) -> o2.getYear().compareTo(o1.getYear()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Saving process error", e);
            throw new DomainException(ErrorMessages.DOMAIN_ERROR_E9999);
        }
    }

    private List<RecordByMonthItem> collectAllMonthsRecords(List<RecordByMonthEntity> entityList) {
        return entityList.stream()
                .map(record ->
                        new RecordByMonthItem(record.getType(), record.getVolume(), record.getDate(), record.getPrice(), record.getTotalPrice(), record.getDriverId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<FuelConsumption> getFuelConsumption(Long driverId, Integer year) throws DomainException {
        try {
            final List<FuelConsumptionEntity> records = purchaseRepository.getFuelConsumption(driverId, year);
            final Map<String, List<FuelConsumptionEntity>> yearsMonth = records.stream().collect(Collectors.groupingBy(r -> r.getYear() + ":" + r.getMonth()));
            List<FuelConsumption> result = new ArrayList<>();

            for (Map.Entry<String, List<FuelConsumptionEntity>> entry : yearsMonth.entrySet()) {
                List<FuelConsumptionFuelType> resultRecordList = entry.getValue().stream()
                        .map(e ->
                                new FuelConsumptionFuelType(e.getType(), e.getVolume(), e.getAveragePrice(), e.getTotalPrice()))
                        .collect(Collectors.toList());

                String[] yearMonth = entry.getKey().split(":");
                result.add(new FuelConsumption(Integer.valueOf(yearMonth[0]), yearMonth[1], resultRecordList));
            }
            result.sort(Comparator.comparing(FuelConsumption::getYear).reversed().thenComparing(FuelConsumption::getMonth));
            return result;
        } catch (Exception e) {
            logger.error("Saving process error", e);
            throw new DomainException(ErrorMessages.DOMAIN_ERROR_E9999);
        }
    }
}
