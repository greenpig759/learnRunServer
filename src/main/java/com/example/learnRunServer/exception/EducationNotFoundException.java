package com.example.learnRunServer.exception;

public class EducationNotFoundException extends RuntimeException {
    private String errorCode;

    public EducationNotFoundException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public EducationNotFoundException(String message) {
        super(message);
    }

    public String getErrorCode() {
        return errorCode;
    }
}
