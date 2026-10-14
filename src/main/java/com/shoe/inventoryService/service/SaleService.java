package com.shoe.inventoryService.service;

import com.shoe.inventoryService.dto.request.SaleCreateRequest;
import com.shoe.inventoryService.dto.response.SaleResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface SaleService {

    /**
     * Records a sale and decrements the shoe batch's remaining stock in the
     * same transaction, under a row lock, so concurrent sales can't oversell
     * a batch.
     */
    SaleResponse create(SaleCreateRequest request);

    SaleResponse getById(Long id);

    Page<SaleResponse> search(LocalDate from, LocalDate to, Long paymentModeId,
                               Long shoeBatchId, String query, Pageable pageable);
}
