package com.bymatech.calculateregulationdisarrangement.config;

import com.bymatech.calculateregulationdisarrangement.dto.ErrorInfo;
import com.bymatech.calculateregulationdisarrangement.exception.AuthorizationException;
import com.bymatech.calculateregulationdisarrangement.exception.FailedValidationException;
import com.bymatech.calculateregulationdisarrangement.exception.MarketResponseException;
import com.bymatech.calculateregulationdisarrangement.exception.PositionValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.concurrent.atomic.AtomicInteger;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final AtomicInteger index = new AtomicInteger();

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorInfo> handleException(Exception e) {
        // Create an ErrorInfo object with the necessary error information
        ErrorInfo errorInfo = new ErrorInfo(index.getAndIncrement(), "Internal Server Error", e.getMessage());
        e.printStackTrace();
        return new ResponseEntity<>(errorInfo, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(FailedValidationException.class)
    public ResponseEntity<ErrorInfo> handleFailedValidationException(FailedValidationException e) {
        ErrorInfo errorInfo = new ErrorInfo(index.getAndIncrement(), "Failed Validation Error", e.getMessage());
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PositionValidationException.class)
    public ResponseEntity<ErrorInfo> handleFailedValidationException(PositionValidationException e) {
        ErrorInfo errorInfo = new ErrorInfo(index.getAndIncrement(),"Position Validation Error", e.getMessage());
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MarketResponseException.class)
    public ResponseEntity<ErrorInfo> handleMarketResponseException(MarketResponseException e) {
        ErrorInfo errorInfo = new ErrorInfo(index.getAndIncrement(),"Market not available Error", e.getMessage());
        return new ResponseEntity<>(errorInfo, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<ErrorInfo> handleAuthorizationException(AuthorizationException e) {
        ErrorInfo errorInfo = new ErrorInfo(index.getAndIncrement(),"Authorization Error", e.getMessage());
        return new ResponseEntity<>(errorInfo, HttpStatus.UNAUTHORIZED);
    }
}
