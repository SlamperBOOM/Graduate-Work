package com.slamperboom.backend.exceptions.exceptions;

import com.slamperboom.backend.exceptions.AppException;
import org.springframework.http.HttpStatus;

public class FileParserException extends AppException {
    public <ErrorType extends Enum<ErrorType>> FileParserException(Enum<ErrorType> error) {
        super(error, HttpStatus.BAD_REQUEST);
    }
}
