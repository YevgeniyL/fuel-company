package com.fuelcompany.domain.errors;

import lombok.Getter;

@Getter
public class DomainException extends RuntimeException {

    private int code;
    private String message;

    public DomainException(ErrorMessages error) {
        this.code = error.getCode();
        this.message = error.getMessage();
    }
}