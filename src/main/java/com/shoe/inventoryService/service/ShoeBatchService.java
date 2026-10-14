package com.shoe.inventoryService.service;

import com.shoe.inventoryService.dto.request.RestockRequest;
import com.shoe.inventoryService.dto.request.ShoeBatchCreateRequest;
import com.shoe.inventoryService.dto.request.ShoeBatchUpdateRequest;
import com.shoe.inventoryService.dto.response.ShoeBatchResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ShoeBatchService {

    ShoeBatchResponse create(ShoeBatchCreateRequest request);

    ShoeBatchResponse update(Long id, ShoeBatchUpdateRequest request);

    ShoeBatchResponse restock(Long id, RestockRequest request);

    ShoeBatchResponse getById(Long id);

    Page<ShoeBatchResponse> search(String query, String size, String shoeSize,boolean lowStockOnly,
                                    boolean outOfStockOnly, Pageable pageable);

    void delete(Long id);
}
