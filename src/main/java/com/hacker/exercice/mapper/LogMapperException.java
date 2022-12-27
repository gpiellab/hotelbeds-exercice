package com.hacker.exercice.mapper;

public class LogMapperException extends RuntimeException {

    public LogMapperException(String message) {
        super(message);
    }
    
    
    public LogMapperException(String message, Throwable cause) {
        super(message, cause);
    }
}
