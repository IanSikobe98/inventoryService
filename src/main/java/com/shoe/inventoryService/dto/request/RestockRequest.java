package com.shoe.inventoryService.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * Body for POST /api/shoe-batches/{id}/restock — adds newly arrived pairs
 * to an existing batch. Increments both quantityReceived and quantityRemaining.
 */
public record RestockRequest(

        @NotNull(message = "quantity is required")
        @Min(value = 1, message = "quantity must be at least 1")
        Integer quantity
) {
}
