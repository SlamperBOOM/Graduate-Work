package com.slamperboom.backend.exceptions.exceptions;

import com.slamperboom.backend.exceptions.AppException;

public class PredictionException extends AppException {
    public <ErrorType extends Enum<ErrorType>> PredictionException(Enum<ErrorType> error) {
        super(error);
    }
}
