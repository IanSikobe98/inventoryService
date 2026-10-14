package com.shoe.inventoryService.dto.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

/**
 * Body for POST /api/shoe-batches — registers a new box of shoes.
 * quantityReceived seeds quantityRemaining; from then on, remaining stock
 * only changes via sales and restocks.
 */
public record ShoeBatchCreateRequest(

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

        @NotNull(message = "quantityReceived is required")
        @Min(value = 0, message = "quantityReceived cannot be negative")
        Integer quantityReceived,

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
