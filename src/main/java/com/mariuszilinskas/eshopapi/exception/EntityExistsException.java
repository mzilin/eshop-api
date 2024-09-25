package com.mariuszilinskas.eshopapi.exception;

public class EntityExistsException extends RuntimeException {

    public EntityExistsException(Class<?> entity, String type, Object value) {
        super(entity.getSimpleName() + " with " + type + " '" + value + "' already exists");
    }
}

