package com.bymatech.calculateregulationdisarrangement.exception;

/**
 * Indicates a business not compliance scenario
 */
public class FailedValidationException extends RuntimeException {

    public FailedValidationException(String message) {
        super(message);
    }
}
