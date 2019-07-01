package com.fuelcompany.infrastructure.repository;

import com.fuelcompany.domain.aggregateModels.purchase.entity.PurchaseEntity;
import com.fuelcompany.domain.aggregateModels.report.entity.FuelConsumptionEntity;
import com.fuelcompany.domain.aggregateModels.report.entity.RecordByMonthEntity;
import com.fuelcompany.domain.aggregateModels.report.entity.TotalByMonthEntity;
import com.fuelcompany.domain.repository.PurchaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Service
public class PurchaseRepositoryImpl implements PurchaseRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    private PurchaseSimpleRepository simpleRepository;

    @Override
    @Transactional
    public PurchaseEntity save(PurchaseEntity report) {
        return simpleRepository.save(report);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TotalByMonthEntity> getAmountByMonths(Long driverId) {
        String builder =
                "SELECT EXTRACT(YEAR FROM p.DATE) AS year, EXTRACT(MONTH FROM p.DATE) AS month, SUM(p.VOLUME * p.PRICE) AS total" +
                        " FROM PURCHASE p " +
                        (driverId != null ? "WHERE p.DRIVER_ID = " + driverId : "") +
                        " GROUP BY year, month " +
                        " ORDER BY year DESC, month ASC";
        return jdbcTemplate.query(builder, new TotalByMonthMapper());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecordByMonthEntity> getReportByMonth(int monthsNumber, Long driverId, Integer year) {
        String sql =
                "SELECT type.NAME AS type," +
                        " p.VOLUME AS volume," +
                        " p.DATE AS purchaseDate," +
                        " p.PRICE AS price," +
                        " p.DRIVER_ID AS driverId," +
                        " EXTRACT(YEAR FROM p.DATE) AS year," +
                        " EXTRACT(MONTH FROM p.DATE) AS month," +
                        " p.VOLUME * p.PRICE AS totalPrice" +
                        " FROM PURCHASE p" +
                        " JOIN FUEL_TYPE type on p.FUEL_TYPE_ID = type.ID" +
                        " WHERE EXTRACT(MONTH FROM p.DATE) = " + monthsNumber +
                        (year != null ? " AND EXTRACT(YEAR FROM p.DATE) = " + year : "") +
                        (driverId != null ? " AND p.DRIVER_ID = " + driverId : "") +
                        " ORDER BY year DESC, month ASC";
        return jdbcTemplate.query(sql, new MonthMapper());
    }

    @Override
    @Transactional(readOnly = true)
    public List<FuelConsumptionEntity> getFuelConsumption(Long driverId, Integer year) {
        String sql =
                "SELECT type.NAME AS type," +
                        " SUM(p.VOLUME) AS volume," +
                        " AVG(p.PRICE) AS averagePrice," +
                        " SUM(p.VOLUME * p.PRICE) AS totalPrice," +
                        " EXTRACT(YEAR FROM p.DATE) AS year," +
                        " EXTRACT(MONTH FROM p.DATE) AS month" +
                        " FROM PURCHASE p" +
                        " JOIN FUEL_TYPE type on p.FUEL_TYPE_ID = type.ID" +
                        getFuelConsumptionPredicates(driverId, year) +
                        " GROUP BY year, month, type" +
                        " ORDER BY year DESC, month ASC";
        return jdbcTemplate.query(sql, new FuelConsumptionMapper());
    }

    private StringBuilder getFuelConsumptionPredicates(Long driverId, Integer year) {
        StringBuilder predicate = new StringBuilder();
        if (driverId != null) predicate.append(" p.DRIVER_ID = ").append(driverId);
        if (year != null) {
            if (predicate.length() > 0) predicate.append(" AND");
            predicate.append(" EXTRACT(YEAR FROM p.DATE) = ").append(year);
        }
        if (predicate.length() > 0) predicate.insert(0, " WHERE");
        return predicate;
    }

    private static final class FuelConsumptionMapper implements RowMapper<FuelConsumptionEntity> {
        @Override
        public FuelConsumptionEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new FuelConsumptionEntity(
                    rs.getString("type"),
                    rs.getBigDecimal("volume"),
                    rs.getBigDecimal("averagePrice"),
                    rs.getBigDecimal("totalPrice"),
                    rs.getInt("year"),
                    rs.getString("month")
            );
        }
    }

    private static final class TotalByMonthMapper implements RowMapper<TotalByMonthEntity> {
        @Override
        public TotalByMonthEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new TotalByMonthEntity(
                    rs.getInt("year"),
                    rs.getString("month"),
                    rs.getBigDecimal("total")
            );
        }
    }

    private static final class MonthMapper implements RowMapper<RecordByMonthEntity> {
        @Override
        public RecordByMonthEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new RecordByMonthEntity(
                    rs.getInt("year"),
                    rs.getString("type"),
                    rs.getBigDecimal("volume"),
                    rs.getDate("date").toLocalDate(),
                    rs.getBigDecimal("price"),
                    rs.getBigDecimal("totalPrice"),
                    rs.getInt("driverId")
            );
        }
    }
}