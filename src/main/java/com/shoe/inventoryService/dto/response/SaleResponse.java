package com.shoe.inventoryService.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SaleResponse(
        Long id,
        Long shoeBatchId,
        String shoeCode,
        String shoeDescription,
        BigDecimal actualSellingPrice,
        Integer quantitySold,
        Long paymentModeId,
        String paymentModeName,
        BigDecimal totalAmount,
        String remarks,
        LocalDateTime createdAt,
        Long createdBy
) {
}
