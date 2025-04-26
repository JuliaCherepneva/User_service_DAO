package com.homework.exception;

public class EntitySaveException extends RuntimeException {
    public EntitySaveException(String message, Throwable cause) {
        super(message, cause);
    }
}
