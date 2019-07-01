package com.fuelcompany.infrastructure.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
class ApiErrorMessage {
    ApiError error;

    public ApiErrorMessage(int status, int errorCode, String message) {
        this.error = new ApiError(status, errorCode, message);
    }

    @Getter
    @AllArgsConstructor
    private class ApiError {
        private int status;
        private int errorCode;
        private String message;
    }
}
