package com.stockguard.stockguard.exception;

public class ResourceNotFoundException extends IllegalArgumentException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
