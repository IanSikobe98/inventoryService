package com.shoe.inventoryService.mapper;

import com.shoe.inventoryService.dto.response.ShoeBatchResponse;
import com.shoe.inventoryService.entity.ShoeBatch;

public final class ShoeBatchMapper {

    /** Batches at or below this remaining count are flagged low-stock in responses. */
    public static final int LOW_STOCK_THRESHOLD = 5;

    private ShoeBatchMapper() {
    }

    public static ShoeBatchResponse toResponse(ShoeBatch b) {
        int remaining = b.getQuantityRemaining() == null ? 0 : b.getQuantityRemaining();
        return new ShoeBatchResponse(
                b.getId(),
                b.getShoeCode(),
                b.getSize(),
                b.getType(),
                b.getColour(),
                b.getPictureUrl(),
                b.getQuantityReceived(),
                b.getQuantityRemaining(),
                b.getCostPerShoe(),
                b.getWholesalePrice(),
                b.getRetailPrice(),
                remaining > 0 && remaining <= LOW_STOCK_THRESHOLD,
                remaining <= 0,
                b.getCreatedAt(),
                b.getUpdatedAt(),
                b.getCreatedBy(),
                b.getUpdatedBy()
        );
    }
}
