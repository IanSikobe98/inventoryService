package com.shoe.inventoryService.exception;

/**
 * Thrown when a requested entity (shoe batch, payment mode, sale, ...) does not exist.
 * Mapped to HTTP 404 by {@link GlobalExceptionHandler}.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public static ResourceNotFoundException forEntity(String entityName, Object id) {
        return new ResourceNotFoundException(entityName + " with id " + id + " was not found");
    }
}
