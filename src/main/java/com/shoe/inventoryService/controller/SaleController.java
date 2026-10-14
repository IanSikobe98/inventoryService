package com.shoe.inventoryService.controller;

import com.shoe.inventoryService.dto.request.SaleCreateRequest;
import com.shoe.inventoryService.dto.response.SaleResponse;
import com.shoe.inventoryService.service.SaleService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/sales")
public class SaleController {

    private final SaleService saleService;

    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    /** Record a sale. Decrements the shoe batch's remaining stock in the same transaction. */
    @PostMapping
    public ResponseEntity<SaleResponse> create(@Valid @RequestBody SaleCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(saleService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SaleResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(saleService.getById(id));
    }

    /**
     * Sales log with filters.
     * Example: GET /api/sales?from=2026-09-01&to=2026-09-22&paymentModeId=2&query=nike
     */
    @GetMapping
    public ResponseEntity<Page<SaleResponse>> search(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(required = false) Long paymentModeId,
            @RequestParam(required = false) Long shoeBatchId,
            @RequestParam(required = false) String query,
            @PageableDefault(size = 25, sort = "createdAt", direction = org.springframework.data.domain.Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(saleService.search(from, to, paymentModeId, shoeBatchId, query, pageable));
    }
}
