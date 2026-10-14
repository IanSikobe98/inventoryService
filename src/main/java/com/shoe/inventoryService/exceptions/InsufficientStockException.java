package com.shoe.inventoryService.exception;

/**
 * Thrown when a sale is recorded for more pairs than remain in a shoe batch.
 * Mapped to HTTP 409 by {@link GlobalExceptionHandler}.
 */
public class InsufficientStockException extends RuntimeException {

    public InsufficientStockException(String message) {
        super(message);
    }
}
