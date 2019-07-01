package com.fuelcompany.infrastructure.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalServerError extends ApiException {
    public InternalServerError(int errorCode, String errorMessage) {
        super(errorCode, errorMessage);
    }
}
