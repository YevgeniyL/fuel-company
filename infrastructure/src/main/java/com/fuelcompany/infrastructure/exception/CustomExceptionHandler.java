package com.fuelcompany.infrastructure.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.core.annotation.AnnotatedElementUtils.findMergedAnnotation;


/**
 * Converter domain exceptions to API exception with http-statuses
 */
@ControllerAdvice
class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ApiException.class)
    @ResponseBody
    ResponseEntity<ApiErrorMessage> handleRequestErrorMyException(ApiException exception) {
        HttpStatus responseStatus = resolveAnnotatedResponseStatus(exception);
        ApiErrorMessage apiErrorMessage = new ApiErrorMessage(responseStatus.value(), exception.getErrorCode(), exception.getMessage());
        return new ResponseEntity<>(apiErrorMessage, responseStatus);
    }

    HttpStatus resolveAnnotatedResponseStatus(Exception exception) {
        ResponseStatus annotation = findMergedAnnotation(exception.getClass(), ResponseStatus.class);
        if (annotation != null) {
            return annotation.value();
        }
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
