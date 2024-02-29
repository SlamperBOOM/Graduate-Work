package com.slamperboom.backend.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AppException extends RuntimeException{
    private final String error;
    private final HttpStatus status;

    public <ErrorType extends Enum<ErrorType>> AppException(Enum<ErrorType> error) {
        this.error = error.toString();
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public <ErrorType extends Enum<ErrorType>> AppException(Enum<ErrorType> error, HttpStatus status) {
        this.error = error.toString();
        this.status = status;
    }
}
