package com.shoe.inventoryService.dto.request;

import jakarta.validation.constraints.NotNull;

/** Body for PATCH /api/payment-modes/{id}/active. */
public record PaymentModeActiveRequest(

        @NotNull(message = "active is required")
        Boolean active
) {
}
