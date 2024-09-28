package com.example.booktickets.exception;

public class CustomServiceException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public CustomServiceException(String message) {
        super(message);
    }

    public CustomServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
