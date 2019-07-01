package com.fuelcompany.application.web;

import com.fuelcompany.domain.aggregateModels.purchase.entity.FuelTypeEntity;
import com.fuelcompany.domain.aggregateModels.purchase.entity.PurchaseEntity;
import com.fuelcompany.domain.repository.FuelTypeRepository;
import com.fuelcompany.domain.repository.PurchaseRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class ReportTest {

    @Autowired
    private PurchaseRepository purchaseRepository;
    @Autowired
    private FuelTypeRepository fuelTypeRepository;
    @Autowired
    private MockMvc mockMvc;

    /**
     * 1) test for total spent amount of money grouped by month
     * 2) list fuel consumption records for specified month (each row should contain: fuel type, volume, date, price, total price, driver ID)
     *
     * @throws Exception
     */
    @Test
    @Transactional
    @Rollback
    public void getTotalByMonthAll() throws Exception {
        /*
         * GET http://localhost:8080/reports/amount
         * Response structure:
         * [
         *     {
         *         "year": 2037,
         *         "month": "SEPTEMBER",
         *         "total": 195
         *     },
         *     {
         *         "year": 2018,
         *         "month": "JANUARY",
         *         "total": 52.5
         *     }
         * ]
         */

        //test for expected empty data
        this.mockMvc.perform(get("/reports/amount"))
                .andExpect(status().isNoContent())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("[0].year").doesNotHaveJsonPath())
                .andDo(print());


        double mayVolume1 = 5.0;
        double mayVolume2 = 10.0;
        double mayVolume3 = 20.5;
        double mayVolume4 = 40.5;
        int price1 = 20, price2 = 20;
        int price3 = 10, price4 = 10;
        FuelTypeEntity d = fuelTypeRepository.findActiveByName("D").get();
        FuelTypeEntity s = fuelTypeRepository.findActiveByName("98").get();
        purchaseRepository.save(new PurchaseEntity(d, BigDecimal.valueOf(mayVolume1), BigDecimal.valueOf(price1), 1L, LocalDate.of(2017, 5, 1)));
        purchaseRepository.save(new PurchaseEntity(s, BigDecimal.valueOf(mayVolume2), BigDecimal.valueOf(price2), 1L, LocalDate.of(2017, 5, 20)));
        purchaseRepository.save(new PurchaseEntity(d, BigDecimal.valueOf(mayVolume3), BigDecimal.valueOf(price3), 2L, LocalDate.of(2017, 5, 3)));
        purchaseRepository.save(new PurchaseEntity(d, BigDecimal.valueOf(mayVolume4), BigDecimal.valueOf(price4), 2L, LocalDate.of(2017, 6, 20)));


        //test for total spent amount of money grouped by month
        //calculate total by two months in one year
        this.mockMvc.perform(get("/reports/amount"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("[0].year").value(2017))
                .andExpect(jsonPath("[0].month").value("MAY"))
                .andExpect(jsonPath("[0].total").value(mayVolume1 * price1 + mayVolume2 * price2 + mayVolume3 * price3))
                .andExpect(jsonPath("[1].month").value("JUNE"))
                .andExpect(jsonPath("[1].total").value(mayVolume4 * price4))
                .andDo(print());

        //test for total spent amount of money grouped by month
        //calculate total by driverId in one year
        this.mockMvc.perform(get("/reports/amount?driverId=1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("[0].year").value(2017))
                .andExpect(jsonPath("[0].month").value("MAY"))
                .andExpect(jsonPath("[0].total").value(mayVolume1 * price1 + mayVolume2 * price2))
                .andExpect(jsonPath("[1].month").doesNotHaveJsonPath())
                .andExpect(jsonPath("[1].total").doesNotHaveJsonPath())
                .andDo(print());



        /*
         *GET http://localhost:8080/reports/months/6?driverId=2&year=2018
         *
         *[
         *     {
         *         "year": 2018,
         *         "records": [
         *             {
         *                 "type": "D",
         *                 "volume": 10,
         *                 "date": "2018-06-09",
         *                 "price": 3.25,
         *                 "totalPrice": 32.5,
         *                 "driverId": 2
         *             }
         *         ]
         *     }
         * ]
         */
        //list fuel consumption records for specified month (each row should contain: fuel type, volume, date, price, total price, driver ID)
        //get not exist month
        this.mockMvc.perform(get("/reports/months/1"))
                .andExpect(status().isNoContent())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("[0].year").doesNotHaveJsonPath())
                .andDo(print());

        //list fuel consumption records for specified month (each row should contain: fuel type, volume, date, price, total price, driver ID)
        //get total by month N5
        this.mockMvc.perform(get("/reports/months/5"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("[0].year").value(2017))
                .andExpect(jsonPath("[0].records").isArray())
                .andExpect(jsonPath("[1].records").doesNotExist())
                .andExpect(jsonPath("[0].records[0].type").value(d.getName()))
                .andExpect(jsonPath("[0].records[1].type").value(s.getName()))
                .andExpect(jsonPath("[0].records[2].type").value(d.getName()))
                .andExpect(jsonPath("[0].records[0].volume").value(mayVolume1))
                .andExpect(jsonPath("[0].records[1].volume").value(mayVolume2))
                .andExpect(jsonPath("[0].records[2].volume").value(mayVolume3))
                .andExpect(jsonPath("[0].records[0].totalPrice").value(mayVolume1 * price1))
                .andExpect(jsonPath("[0].records[1].totalPrice").value(mayVolume2 * price2))
                .andExpect(jsonPath("[0].records[2].totalPrice").value(mayVolume3 * price3))
                .andExpect(jsonPath("[0].records[0].driverId").value(1L))
                .andExpect(jsonPath("[0].records[1].driverId").value(1L))
                .andExpect(jsonPath("[0].records[2].driverId").value(2L))
                .andExpect(jsonPath("[1].year").doesNotExist())
                .andDo(print());

        //list fuel consumption records for specified month (each row should contain: fuel type, volume, date, price, total price, driver ID)
        //not exist month with real driverId
        this.mockMvc.perform(get("/reports/months/3?driverId=2"))
                .andExpect(status().isNoContent())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("[0].year").doesNotHaveJsonPath());

        //list fuel consumption records for specified month (each row should contain: fuel type, volume, date, price, total price, driver ID)
        //get total by 5 month driverId=2
        this.mockMvc.perform(get("/reports/months/6?driverId=2"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("[0].year").value(2017))
                .andExpect(jsonPath("[0].records").isArray())
                .andExpect(jsonPath("[0].records[0].type").value(d.getName()))
                .andExpect(jsonPath("[0].records[0].volume").value(40.5))
                .andExpect(jsonPath("[0].records[0].date").value("2017-06-20"))
                .andExpect(jsonPath("[0].records[0].price").value(10.0))
                .andExpect(jsonPath("[0].records[0].totalPrice").value(405))
                .andExpect(jsonPath("[0].records[0].driverId").value(2))
                .andExpect(jsonPath("[0].records[1].type").doesNotExist())
                .andExpect(jsonPath("[1].year").doesNotExist())
                .andDo(print());

        //list fuel consumption records for specified month (each row should contain: fuel type, volume, date, price, total price, driver ID)
        //check years by with driverId=3
        int year2018 = 2018;
        int year2017 = 2017;
        double volume40_5 = 40.5;
        int price10 = 10;
        int price30 = 30;
        purchaseRepository.save(new PurchaseEntity(d, BigDecimal.valueOf(volume40_5), BigDecimal.valueOf(price10), 3L, LocalDate.of(year2017, 8, 20)));
        purchaseRepository.save(new PurchaseEntity(d, BigDecimal.valueOf(volume40_5), BigDecimal.valueOf(price30), 3L, LocalDate.of(year2018, 8, 20)));
        this.mockMvc.perform(get("/reports/months/8?driverId=3"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("[0].year").value(year2018))
                .andExpect(jsonPath("[0].records").isArray())
                .andExpect(jsonPath("[0].records[0].type").isNotEmpty())
                .andExpect(jsonPath("[0].records[1].type").doesNotExist())
                .andExpect(jsonPath("[0].records[0].totalPrice").value(volume40_5 * price30))
                .andExpect(jsonPath("[1].year").value(year2017))
                .andExpect(jsonPath("[1].records").isArray())
                .andExpect(jsonPath("[1].records[0].driverId").value(3L))
                .andExpect(jsonPath("[1].records[0].totalPrice").value(volume40_5 * price10))
                .andExpect(jsonPath("[1].records[1].driverId").doesNotExist())
                .andDo(print());
    }


    /**
     * statistics for each month, list fuel consumption records grouped by fuel type (each row should contain:
     * fuel type, volume, average price, total price)
     *
     * @throws Exception
     */
    @Test
    @Transactional
    @Rollback
    public void getFuelConsumption() throws Exception {
        /*
         * GET http://localhost:8080/reports/consumption
         * [
         *     {
         *         "year": 2018,
         *         "month": "2",
         *         "fuelTypes": [
         *             {
         *                 "fuelType": "D",
         *                 "volume": 10,
         *                 "averagePrice": 3.25,
         *                 "totalPrice": 32.5
         *             }
         *         ]
         *     }
         *  ]
         */

        //test for expected empty data
        this.mockMvc.perform(get("/reports/consumption?driverId=2&year=2018"))
                .andExpect(status().isNoContent())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("[0].year").doesNotHaveJsonPath())
                .andDo(print());


        double mayVolume1 = 5.0;
        double mayVolume2 = 10.0;
        double mayVolume3 = 20.5;
        double mayVolume4 = 40.5;
        int price1 = 20, price2 = 20;
        int price3 = 10, price4 = 10;
        FuelTypeEntity d = fuelTypeRepository.findActiveByName("D").get();
        purchaseRepository.save(new PurchaseEntity(d, BigDecimal.valueOf(mayVolume1), BigDecimal.valueOf(price1), 1L, LocalDate.of(2017, 5, 1)));
        purchaseRepository.save(new PurchaseEntity(d, BigDecimal.valueOf(mayVolume2), BigDecimal.valueOf(price2), 2L, LocalDate.of(2017, 5, 3)));
        purchaseRepository.save(new PurchaseEntity(d, BigDecimal.valueOf(mayVolume3), BigDecimal.valueOf(price3), 2L, LocalDate.of(2017, 6, 20)));

        //test for existing all inserted records
        this.mockMvc.perform(get("/reports/consumption"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("[0].year").value(2017))
                .andExpect(jsonPath("[0].month").value(5))
                .andExpect(jsonPath("[0].fuelTypes").isArray())
                .andExpect(jsonPath("[0].fuelTypes[0].fuelType").value(d.getName()))
                .andExpect(jsonPath("[0].fuelTypes[0].volume").value(mayVolume1 + mayVolume2))
                .andExpect(jsonPath("[0].fuelTypes[0].averagePrice").value((price1 + price2) / 2))
                .andExpect(jsonPath("[0].fuelTypes[0].totalPrice").value(mayVolume1 * price1 + mayVolume2 * price2))
                .andExpect(jsonPath("[0].fuelTypes[1]").doesNotExist())

                .andExpect(jsonPath("[1].year").value(2017))
                .andExpect(jsonPath("[1].month").value(6))
                .andExpect(jsonPath("[1].fuelTypes").isArray())
                .andExpect(jsonPath("[1].fuelTypes[0].fuelType").value(d.getName()))
                .andExpect(jsonPath("[1].fuelTypes[0].volume").value(mayVolume3))
                .andExpect(jsonPath("[1].fuelTypes[0].averagePrice").value((price3)))
                .andExpect(jsonPath("[1].fuelTypes[0].totalPrice").value(mayVolume3 * price3))
                .andExpect(jsonPath("[1].fuelTypes[1]").doesNotExist())
                .andDo(print());

        //test for not existing driverId
        this.mockMvc.perform(get("/reports/consumption?driverId=9999999"))
                .andExpect(status().isNoContent())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("[0]").doesNotExist())
                .andExpect(jsonPath("[0]").doesNotHaveJsonPath());

        //test for existing all by driverId
        this.mockMvc.perform(get("/reports/consumption?driverId=2"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("[0].year").value(2017))
                .andExpect(jsonPath("[0].month").value(5))
                .andExpect(jsonPath("[0].fuelTypes").isArray())
                .andExpect(jsonPath("[0].fuelTypes[0].fuelType").value(d.getName()))
                .andExpect(jsonPath("[0].fuelTypes[0].volume").value(mayVolume2))
                .andExpect(jsonPath("[0].fuelTypes[0].averagePrice").value(price2))
                .andExpect(jsonPath("[0].fuelTypes[0].totalPrice").value(mayVolume2 * price2))
                .andExpect(jsonPath("[0].fuelTypes[1]").doesNotExist())

                .andExpect(jsonPath("[1].year").value(2017))
                .andExpect(jsonPath("[1].month").value(6))
                .andExpect(jsonPath("[1].fuelTypes").isArray())
                .andExpect(jsonPath("[1].fuelTypes[0].fuelType").value(d.getName()))
                .andExpect(jsonPath("[1].fuelTypes[0].volume").value(mayVolume3))
                .andExpect(jsonPath("[1].fuelTypes[0].averagePrice").value((price3)))
                .andExpect(jsonPath("[1].fuelTypes[0].totalPrice").value(mayVolume3 * price3))
                .andExpect(jsonPath("[1].fuelTypes[1]").doesNotExist())
                .andDo(print());

        //test for not existing year
        this.mockMvc.perform(get("/reports/consumption?year=2018"))
                .andExpect(status().isNoContent())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("[0]").doesNotExist())
                .andExpect(jsonPath("[0]").doesNotHaveJsonPath());

        //test for not existing year
        this.mockMvc.perform(get("/reports/consumption?year=2017"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("[0].year").value(2017))
                .andExpect(jsonPath("[1].year").value(2017))
                .andExpect(jsonPath("[2].year").doesNotExist())
                .andExpect(jsonPath("[2].year").doesNotHaveJsonPath());

        purchaseRepository.save(new PurchaseEntity(d, BigDecimal.valueOf(mayVolume4), BigDecimal.valueOf(price4), 2L, LocalDate.of(2018, 9, 20)));

        //test for not existing year
        this.mockMvc.perform(get("/reports/consumption?year=2018"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("[0].year").value(2018))
                .andExpect(jsonPath("[1].year").doesNotExist())
                .andExpect(jsonPath("[1].year").doesNotHaveJsonPath())

                .andExpect(jsonPath("[0].month").value(9))
                .andExpect(jsonPath("[0].fuelTypes").isArray())
                .andExpect(jsonPath("[0].fuelTypes[0].fuelType").value(d.getName()))
                .andExpect(jsonPath("[0].fuelTypes[0].volume").value(mayVolume4))
                .andExpect(jsonPath("[0].fuelTypes[0].averagePrice").value(price4))
                .andExpect(jsonPath("[0].fuelTypes[0].totalPrice").value(mayVolume4 * price4))
                .andExpect(jsonPath("[0].fuelTypes[1]").doesNotExist());
    }
}