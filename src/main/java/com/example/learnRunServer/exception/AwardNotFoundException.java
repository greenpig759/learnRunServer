package com.example.learnRunServer.exception;

public class AwardNotFoundException extends RuntimeException {
    public AwardNotFoundException(String message) {
        super(message);
    }
}
