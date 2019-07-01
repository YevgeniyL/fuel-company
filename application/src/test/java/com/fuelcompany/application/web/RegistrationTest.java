package com.fuelcompany.application.web;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fuelcompany.infrastructure.api.registration.ApiPurchase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.File;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class RegistrationTest{

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Transactional
    @Rollback
    public void registrationTest() throws Exception {
        /*
         * POST http://localhost:8080/purchases
         * Incoming structure:
         * {
         *       "fuelType": "D",
         *       "volume":20,
         *       "price": 3.25,
         *       "driverId": 1,
         *       "date": "2037-09-09"
         *   }
         *
         *
         *  Result structure:
         * {
         *     "id": 775,
         *     "volume": 20,
         *     "fuelType": "D",
         *     "price": 3.25,
         *     "driverId": 1,
         *     "date": "2037-09-09"
         * }
         */

        ApiPurchase purchase = new ApiPurchase("D", BigDecimal.valueOf(12.000), BigDecimal.valueOf(3.25), 1L, LocalDate.of(2000, 10, 19));
        String body = (new ObjectMapper()).valueToTree(purchase).toString();
        this.mockMvc.perform(post("/purchases")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
        )
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("fuelType").value("D"))
                .andExpect(jsonPath("price").value(3.25))
                .andExpect(jsonPath("driverId").value(1))
                .andExpect(jsonPath("date").value("2000-10-19"))
                .andDo(print());
    }


    @Test
    @Rollback
    @Transactional
    public void registrationErrors_1001_Test() throws Exception {
        ApiPurchase purchase = new ApiPurchase("D", BigDecimal.valueOf(12.000), BigDecimal.valueOf(3.25), 1L, null);
        String body = (new ObjectMapper()).valueToTree(purchase).toString();
        this.mockMvc.perform(post("/purchases")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
        )
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("error.status").value(422))
                .andExpect(jsonPath("error.errorCode").value(1001))
                .andExpect(jsonPath("error.message").value("Field 'date' is empty"))
                .andDo(print());
    }

    @Test
    @Rollback
    @Transactional
    public void registrationErrors_1002_Test() throws Exception {
        ApiPurchase purchase = new ApiPurchase(null, BigDecimal.valueOf(12.000), BigDecimal.valueOf(3.25), 1L, LocalDate.of(2000, 10, 19));
        String body = (new ObjectMapper()).valueToTree(purchase).toString();
        this.mockMvc.perform(post("/purchases")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
        )
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("error.status").value(422))
                .andExpect(jsonPath("error.errorCode").value(1002))
                .andExpect(jsonPath("error.message").value("Field 'fuelType' is empty"))
                .andDo(print());
    }

    @Test
    @Rollback
    @Transactional
    public void registrationErrors_1003_Test() throws Exception {
        ApiPurchase purchase = new ApiPurchase("D", BigDecimal.valueOf(12.000), null, 1L, LocalDate.of(2000, 10, 19));
        String body = (new ObjectMapper()).valueToTree(purchase).toString();
        this.mockMvc.perform(post("/purchases")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
        )
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("error.status").value(422))
                .andExpect(jsonPath("error.errorCode").value(1003))
                .andExpect(jsonPath("error.message").value("Field 'price' is empty"))
                .andDo(print());
    }

    @Test
    @Rollback
    @Transactional
    public void registrationErrors_1004_Test() throws Exception {
        ApiPurchase purchase = new ApiPurchase("D", BigDecimal.valueOf(12.000), BigDecimal.valueOf(3.25), null, LocalDate.of(2000, 10, 19));
        String body = (new ObjectMapper()).valueToTree(purchase).toString();
        this.mockMvc.perform(post("/purchases")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
        )
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("error.status").value(422))
                .andExpect(jsonPath("error.errorCode").value(1004))
                .andExpect(jsonPath("error.message").value("Field 'driverId' is empty"))
                .andDo(print());
    }

    @Test
    @Rollback
    @Transactional
    public void registrationErrors_1005_Test() throws Exception {
        ApiPurchase purchase = new ApiPurchase("Z", BigDecimal.valueOf(12.000), BigDecimal.valueOf(3.25), 1L, LocalDate.of(2000, 10, 19));
        String body = (new ObjectMapper()).valueToTree(purchase).toString();
        this.mockMvc.perform(post("/purchases")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
        )
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("error.status").value(422))
                .andExpect(jsonPath("error.errorCode").value(1005))
                .andExpect(jsonPath("error.message").value("Fuel type not exist"))
                .andDo(print());
    }

    @Test
    @Rollback
    @Transactional
    public void registrationErrors_1006_Test() throws Exception {
        ApiPurchase purchase = new ApiPurchase("D", null, BigDecimal.valueOf(3.25), 1L, LocalDate.of(2000, 10, 19));
        String body = (new ObjectMapper()).valueToTree(purchase).toString();
        this.mockMvc.perform(post("/purchases")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
        )
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("error.status").value(422))
                .andExpect(jsonPath("error.errorCode").value(1006))
                .andExpect(jsonPath("error.message").value("Field 'volume' is empty"))
                .andDo(print());
    }

    @Test
    @Rollback
    @Transactional
    public void test() throws Exception {
        /*
         * MULTIPART_FORM_DATA_VALUE
         * POST http://localhost:8080/purchases/file
         * Incoming structure:
         *
         * [
         *  {
         *       "fuelType": "D",
         *       "volume":20,
         *       "price": 3.25,
         *       "driverId": 1,
         *       "date": "2037-09-09"
         *   }
         * ]
         */
        assertEquals(0, ((Number) entityManager.createQuery("SELECT count(*) FROM PurchaseEntity p").getSingleResult()).intValue());
        this.mockMvc.perform(MockMvcRequestBuilders.multipart("/purchases/file")
                .file("file", Files.readAllBytes(new File("src/test/resources/multipart.json").toPath()))
                .param("name", "multipart.json")
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
        )
                .andExpect(status().isCreated());

        assertEquals(3, ((Number) entityManager.createQuery("SELECT count(*) FROM PurchaseEntity p").getSingleResult()).intValue());
    }
}
