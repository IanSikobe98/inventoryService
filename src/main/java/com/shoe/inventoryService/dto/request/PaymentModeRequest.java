package com.shoe.inventoryService.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** Body for POST /api/payment-modes. */
public record PaymentModeRequest(

        @NotBlank(message = "name is required")
        @Size(max = 50)
        String name
) {
}
