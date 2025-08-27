package com.example.learnRunServer.exception;

public class QualificationsNotFoundException extends RuntimeException {
    private String errorCode;

    public QualificationsNotFoundException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public QualificationsNotFoundException(String message) {
        super(message);
    }

    public String getErrorCode() {
        return errorCode;
    }
}
