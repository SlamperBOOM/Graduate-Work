package com.slamperboom.backend.exceptions.exceptions;

import com.slamperboom.backend.exceptions.AppException;
import org.springframework.http.HttpStatus;

public class PredictionException extends AppException {
    public <ErrorType extends Enum<ErrorType>> PredictionException(Enum<ErrorType> error) {
        super(error);
    }
}
