package com.slamperboom.backend.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class AppExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {AppException.class})
    protected ResponseEntity<Object> handleAppException(AppException ex, WebRequest webRequest){
        return handleExceptionInternal(ex, new ErrorResponse(ex.getError()),
                new HttpHeaders(), ex.getStatus(), webRequest);
    }
}
