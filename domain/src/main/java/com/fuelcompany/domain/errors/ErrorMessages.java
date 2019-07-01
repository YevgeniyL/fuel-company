package com.fuelcompany.domain.errors;


import lombok.Getter;


/**
 * All business logic errors
 */

@Getter
public enum ErrorMessages {
    DOMAIN_ERROR_1001(1001, "Field 'date' is empty"),
    DOMAIN_ERROR_1002(1002, "Field 'fuelType' is empty"),
    DOMAIN_ERROR_1003(1003, "Field 'price' is empty"),
    DOMAIN_ERROR_1004(1004, "Field 'driverId' is empty"),
    DOMAIN_ERROR_1005(1005, "Fuel type not exist"),
    DOMAIN_ERROR_1006(1006, "Field 'volume' is empty"),
    DOMAIN_ERROR_1007(1007, "Field 'volume' need to be a positive"),
    DOMAIN_ERROR_1008(1008, "Field 'price' need to be a positive"),
    DOMAIN_ERROR_1009(1009, "Field 'driverId' need to be a positive"),

    DOMAIN_ERROR_E1050(1050, "Empty list of purchase"),
    DOMAIN_ERROR_E1051(1051, "Too many elements for save"),

    DOMAIN_ERROR_E9999(9999, "Internal server error"),
    ;

    private int code;
    private String message;

    ErrorMessages(int code, String message) {
        this.code = code;
        this.message = message;
    }
}