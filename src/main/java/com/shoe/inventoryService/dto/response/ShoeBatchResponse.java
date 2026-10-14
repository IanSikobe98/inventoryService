package com.shoe.inventoryService.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ShoeBatchResponse(
        Long id,
        String shoeCode,
        String size,
        String type,
        String colour,
        String pictureUrl,
        Integer quantityReceived,
        Integer quantityRemaining,
        BigDecimal costPerShoe,
        BigDecimal wholesalePrice,
        BigDecimal retailPrice,
        boolean lowStock,
        boolean outOfStock,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Long createdBy,
        Long updatedBy
) {
}
