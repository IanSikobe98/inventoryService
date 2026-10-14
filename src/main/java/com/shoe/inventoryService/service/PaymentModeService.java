package com.shoe.inventoryService.service;

import com.shoe.inventoryService.dto.request.PaymentModeRequest;
import com.shoe.inventoryService.dto.response.PaymentModeResponse;

import java.util.List;

public interface PaymentModeService {

    PaymentModeResponse create(PaymentModeRequest request);

    List<PaymentModeResponse> findAll(boolean activeOnly);

    PaymentModeResponse setActive(Long id, boolean active);
}
