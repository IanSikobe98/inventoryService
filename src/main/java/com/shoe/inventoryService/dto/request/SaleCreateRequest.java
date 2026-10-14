package com.shoe.inventoryService.dto.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

/**
 * Body for POST /api/sales — records a sale against a shoe batch.
 * totalAmount is always computed server-side (actualSellingPrice * quantitySold);
 * a value sent by the client is ignored.
 */
public record SaleCreateRequest(

        @NotNull(message = "shoeBatchId is required")
        Long shoeBatchId,

        @NotNull(message = "actualSellingPrice is required")
        @DecimalMin(value = "0.0", message = "actualSellingPrice cannot be negative")
        BigDecimal actualSellingPrice,

        @NotNull(message = "quantitySold is required")
        @Min(value = 1, message = "quantitySold must be at least 1")
        Integer quantitySold,

        @NotNull(message = "paymentModeId is required")
        Long paymentModeId,

        @Size(max = 1000)
        String remarks
) {
}
