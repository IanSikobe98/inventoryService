package com.shoe.inventoryService.exception;

/**
 * Thrown when an operation would violate a uniqueness rule
 * (e.g. a shoe code or payment mode name that already exists).
 * Mapped to HTTP 409 by {@link GlobalExceptionHandler}.
 */
public class DuplicateResourceException extends RuntimeException {

    public DuplicateResourceException(String message) {
        super(message);
    }
}
