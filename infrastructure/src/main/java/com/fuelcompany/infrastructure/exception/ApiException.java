package com.fuelcompany.infrastructure.exception;


import com.fuelcompany.domain.errors.DomainException;
import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {
    private int errorCode;
    private String message;

    ApiException(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.message = errorMessage;
    }

    public ApiException(DomainException e) {
        if (e.getCode() == 9999) throw new InternalServerError(e.getCode(), e.getMessage());
        throw new UnprocessableEntityException(e.getCode(), e.getMessage());
    }
}
