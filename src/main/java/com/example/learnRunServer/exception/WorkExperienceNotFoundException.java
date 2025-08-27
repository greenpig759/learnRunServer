package com.example.learnRunServer.exception;

public class WorkExperienceNotFoundException extends RuntimeException {
    private String errorCode;

    public WorkExperienceNotFoundException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public WorkExperienceNotFoundException(String message) {
        super(message);
    }

    public String getErrorCode() {
        return errorCode;
    }
}
