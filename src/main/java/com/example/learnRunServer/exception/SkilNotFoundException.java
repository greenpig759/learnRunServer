package com.example.learnRunServer.exception;

public class SkilNotFoundException extends RuntimeException {
    public SkilNotFoundException(String message) {
        super(message);
    }
}
