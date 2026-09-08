package com.enzo.event.shared.exception;

public abstract class ResourceNotFoundException extends RuntimeException {

    // Use only when necessary
    public ResourceNotFoundException() {
        super("The requested resource was not found.");
    }

    // Use only when necessary
    public ResourceNotFoundException(String message) {
        super(message);
    }

    // Always prefer using only this function
    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s was not found with %s: '%s'", resourceName, fieldName, fieldValue));
    }
}