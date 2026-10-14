package com.shoe.inventoryService.mapper;

import com.shoe.inventoryService.dto.response.SaleResponse;
import com.shoe.inventoryService.entity.Sale;

public final class SaleMapper {

    private SaleMapper() {
    }

    public static SaleResponse toResponse(Sale s) {
        return new SaleResponse(
                s.getId(),
                s.getShoeBatch().getId(),
                s.getShoeBatch().getShoeCode(),
                describe(s),
                s.getActualSellingPrice(),
                s.getQuantitySold(),
                s.getPaymentMode().getId(),
                s.getPaymentMode().getName(),
                s.getTotalAmount(),
                s.getRemarks(),
                s.getCreatedAt(),
                s.getCreatedBy()
        );
    }

    private static String describe(Sale s) {
        var b = s.getShoeBatch();
        return b.getShoeCode() + " · " + b.getType() + " · " + b.getColour();
    }
}
