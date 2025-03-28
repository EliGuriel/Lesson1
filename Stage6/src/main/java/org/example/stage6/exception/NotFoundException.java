package org.example.stage6.exception;

// custom exception class for 404 Not Found
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}