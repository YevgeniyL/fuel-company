package com.fuelcompany.infrastructure.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class UnprocessableEntityException extends ApiException {
    public UnprocessableEntityException(int errorCode, String errorMessage) {
        super(errorCode, errorMessage);
    }
}
