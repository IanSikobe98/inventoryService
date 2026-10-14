package com.shoe.inventoryService.dto.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

/**
 * Body for PUT /api/shoe-batches/{id} — edits a batch's descriptive fields
 * and pricing. quantityReceived / quantityRemaining are intentionally not
 * editable here; use POST /api/shoe-batches/{id}/restock to add stock.
 */
public record ShoeBatchUpdateRequest(

        @NotBlank(message = "shoeCode is required")
        @Size(max = 50)
        String shoeCode,

        @NotBlank(message = "size is required")
        @Size(max = 20)
        String size,

        @NotBlank(message = "type is required")
        @Size(max = 100)
        String type,

        @NotBlank(message = "colour is required")
        @Size(max = 50)
        String colour,


        String pictureUrl,

        @NotNull(message = "costPerShoe is required")
        @DecimalMin(value = "0.0", message = "costPerShoe cannot be negative")
        BigDecimal costPerShoe,

        @NotNull(message = "wholesalePrice is required")
        @DecimalMin(value = "0.0", message = "wholesalePrice cannot be negative")
        BigDecimal wholesalePrice,

        @NotNull(message = "retailPrice is required")
        @DecimalMin(value = "0.0", message = "retailPrice cannot be negative")
        BigDecimal retailPrice
) {
}
