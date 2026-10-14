package com.shoe.inventoryService.mapper;

import com.shoe.inventoryService.dto.response.PaymentModeResponse;
import com.shoe.inventoryService.entity.PaymentMode;

public final class PaymentModeMapper {

    private PaymentModeMapper() {
    }

    public static PaymentModeResponse toResponse(PaymentMode m) {
        return new PaymentModeResponse(m.getId(), m.getName(), m.getActive());
    }
}
