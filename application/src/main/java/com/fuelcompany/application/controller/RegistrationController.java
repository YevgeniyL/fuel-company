package com.fuelcompany.application.controller;

import com.fuelcompany.domain.errors.DomainException;
import com.fuelcompany.domain.service.PurchaseService;
import com.fuelcompany.infrastructure.api.registration.ApiPurchase;
import com.fuelcompany.infrastructure.api.registration.PurchaseTransformer;
import com.fuelcompany.infrastructure.exception.ApiException;
import com.fuelcompany.infrastructure.utils.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Controller with REST requests mapping
 * Registration(save) incoming data
 */
@RestController
@RequestMapping("/purchases")
public class RegistrationController {
    private static Logger logger = LoggerFactory.getLogger(RegistrationController.class);

    @Autowired
    private PurchaseService purchaseService;
    @Autowired
    private PurchaseTransformer transformer;

    /**
     * register one single record
     *
     * @param request
     * @return
     */
    @Transactional
    @PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ApiPurchase save(@RequestBody ApiPurchase request) {
        try {
            return transformer.toREST(purchaseService.save(transformer.toDomain(request)));
        } catch (DomainException e) {
            logger.error("Domain exception", e);
            throw new ApiException(e);
        }
    }

    /**
     * register multiple records in one file
     *
     * @param file
     */
    @Transactional
    @PostMapping(path = "/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void save(@RequestParam(value = "file") MultipartFile file) {
        try {
            purchaseService.save(transformer.toDomain(FileUtil.convertToObjectList(file)));
        } catch (DomainException e) {
            logger.error("Domain exception", e);
            throw new ApiException(e);
        }
    }
}
