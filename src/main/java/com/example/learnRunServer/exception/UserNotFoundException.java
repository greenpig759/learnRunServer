package com.example.learnRunServer.exception;

public class UserNotFoundException extends RuntimeException {
    private String errorCode;

    public UserNotFoundException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public UserNotFoundException(String message) {
        super(message);
    }

    public String getErrorCode() {
        return errorCode;
    }
}