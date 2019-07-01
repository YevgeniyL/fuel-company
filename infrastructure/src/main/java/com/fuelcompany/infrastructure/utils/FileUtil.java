package com.fuelcompany.infrastructure.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fuelcompany.infrastructure.api.registration.ApiPurchase;
import com.fuelcompany.infrastructure.exception.InternalServerError;
import com.fuelcompany.infrastructure.exception.UnprocessableEntityException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

public class FileUtil {
    private static Logger logger = LoggerFactory.getLogger(FileUtil.class);
    private static final ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

    //TODO if needed will create Generic method
    public static List<ApiPurchase> convertToObjectList(MultipartFile file) {
        if (file.isEmpty())
            throw new UnprocessableEntityException(0, "Empty file in request");

        try {
            return mapper.readValue(file.getBytes(), new TypeReference<ArrayList<ApiPurchase>>() {
            });
        } catch (Exception e) {
            logger.error("Convertation from json file to objectList error", e);
            throw new InternalServerError(9999, "Convertation from json file to objectList failed");
        }
    }
}
