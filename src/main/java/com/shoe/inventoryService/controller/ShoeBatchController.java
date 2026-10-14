package com.shoe.inventoryService.controller;

import com.shoe.inventoryService.dto.request.RestockRequest;
import com.shoe.inventoryService.dto.request.ShoeBatchCreateRequest;
import com.shoe.inventoryService.dto.request.ShoeBatchUpdateRequest;
import com.shoe.inventoryService.dto.response.ShoeBatchResponse;
import com.shoe.inventoryService.service.ShoeBatchService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shoe-batches")
public class ShoeBatchController {

    private final ShoeBatchService shoeBatchService;

    public ShoeBatchController(ShoeBatchService shoeBatchService) {
        this.shoeBatchService = shoeBatchService;
    }

    /** Register a new box of shoes. */
    @PostMapping
    public ResponseEntity<ShoeBatchResponse> create(@Valid @RequestBody ShoeBatchCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(shoeBatchService.create(request));
    }

    /** Edit a batch's descriptive fields, photo and pricing. */
    @PutMapping("/{id}")
    public ResponseEntity<ShoeBatchResponse> update(@PathVariable Long id,
                                                      @Valid @RequestBody ShoeBatchUpdateRequest request) {
        return ResponseEntity.ok(shoeBatchService.update(id, request));
    }

    /** Add newly arrived pairs to an existing batch. */
    @PostMapping("/{id}/restock")
    public ResponseEntity<ShoeBatchResponse> restock(@PathVariable Long id,
                                                        @Valid @RequestBody RestockRequest request) {
        return ResponseEntity.ok(shoeBatchService.restock(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShoeBatchResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(shoeBatchService.getById(id));
    }

    /**
     * Search and browse batches.
     * Example: GET /api/shoe-batches?query=nike&size=42&lowStockOnly=true&page=0&size2=20
     */
    @GetMapping
    public ResponseEntity<Page<ShoeBatchResponse>> search(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) String shoeSize,
            @RequestParam(defaultValue = "false") boolean lowStockOnly,
            @RequestParam(defaultValue = "false") boolean outOfStockOnly,
            @PageableDefault(size = 24, sort = "shoeCode") Pageable pageable) {
        return ResponseEntity.ok(shoeBatchService.search(query, size, shoeSize,lowStockOnly, outOfStockOnly, pageable));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        shoeBatchService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
