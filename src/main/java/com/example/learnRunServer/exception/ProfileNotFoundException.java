package com.example.learnRunServer.exception;

public class ProfileNotFoundException extends RuntimeException {
    private String errorCode;

    public ProfileNotFoundException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public ProfileNotFoundException(String message) {
        super(message);
    }

    public String getErrorCode() {
        return errorCode;
    }
}
