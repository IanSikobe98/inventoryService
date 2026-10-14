package com.shoe.inventoryService.dto.response;

public record PaymentModeResponse(
        Long id,
        String name,
        Boolean active
) {
}
