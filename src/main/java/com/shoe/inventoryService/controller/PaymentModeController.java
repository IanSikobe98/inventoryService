package com.shoe.inventoryService.controller;

import com.shoe.inventoryService.dto.request.PaymentModeActiveRequest;
import com.shoe.inventoryService.dto.request.PaymentModeRequest;
import com.shoe.inventoryService.dto.response.PaymentModeResponse;
import com.shoe.inventoryService.service.PaymentModeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payment-modes")
public class PaymentModeController {

    private final PaymentModeService paymentModeService;

    public PaymentModeController(PaymentModeService paymentModeService) {
        this.paymentModeService = paymentModeService;
    }

    @PostMapping
    public ResponseEntity<PaymentModeResponse> create(@Valid @RequestBody PaymentModeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentModeService.create(request));
    }

    /** List payment modes. Pass activeOnly=true to populate a sale-recording dropdown. */
    @GetMapping
    public ResponseEntity<List<PaymentModeResponse>> findAll(
            @RequestParam(defaultValue = "false") boolean activeOnly) {
        return ResponseEntity.ok(paymentModeService.findAll(activeOnly));
    }

    @PatchMapping("/{id}/active")
    public ResponseEntity<PaymentModeResponse> setActive(@PathVariable Long id,
                                                            @Valid @RequestBody PaymentModeActiveRequest request) {
        return ResponseEntity.ok(paymentModeService.setActive(id, request.active()));
    }
}
