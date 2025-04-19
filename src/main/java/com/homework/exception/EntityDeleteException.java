package com.homework.exception;

public class EntityDeleteException extends RuntimeException {
    public EntityDeleteException(String message, Throwable cause) {
        super(message, cause);
    }
}
