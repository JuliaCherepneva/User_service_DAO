package com.homework.exception;

public class EntityUpdateException extends RuntimeException {
    public EntityUpdateException(String message, Throwable cause) {
        super(message, cause);
    }
}
