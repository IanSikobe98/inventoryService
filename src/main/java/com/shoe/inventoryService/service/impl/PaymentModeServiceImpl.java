package com.shoe.inventoryService.service.impl;

import com.shoe.inventoryService.dto.request.PaymentModeRequest;
import com.shoe.inventoryService.dto.response.PaymentModeResponse;
import com.shoe.inventoryService.entity.PaymentMode;
import com.shoe.inventoryService.exception.DuplicateResourceException;
import com.shoe.inventoryService.exception.ResourceNotFoundException;
import com.shoe.inventoryService.mapper.PaymentModeMapper;
import com.shoe.inventoryService.repository.PaymentModeRepository;
import com.shoe.inventoryService.service.PaymentModeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@Transactional
public class PaymentModeServiceImpl implements PaymentModeService {

    private final PaymentModeRepository paymentModeRepository;


    public PaymentModeServiceImpl(PaymentModeRepository paymentModeRepository) {
        this.paymentModeRepository = paymentModeRepository;
    }

    @Override
    public PaymentModeResponse create(PaymentModeRequest request) {
        if (paymentModeRepository.existsByNameIgnoreCase(request.name())) {
            throw new DuplicateResourceException("A payment mode named '" + request.name() + "' already exists");
        }

        PaymentMode mode = PaymentMode.builder()
                .name(request.name().trim())
                .active(true)
                .createdBy(2L)
                .updatedBy(2L)
                .build();
        return PaymentModeMapper.toResponse(paymentModeRepository.save(mode));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentModeResponse> findAll(boolean activeOnly) {
        List<PaymentMode> modes = activeOnly
                ? paymentModeRepository.findByActiveTrue()
                : paymentModeRepository.findAll();
        return modes.stream()
                .sorted(Comparator.comparing(PaymentMode::getName, String.CASE_INSENSITIVE_ORDER))
                .map(PaymentModeMapper::toResponse)
                .toList();
    }

    @Override
    public PaymentModeResponse setActive(Long id, boolean active) {
        PaymentMode mode = paymentModeRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.forEntity("PaymentMode", id));
        mode.setActive(active);
        mode.setUpdatedBy(2L);
        return PaymentModeMapper.toResponse(paymentModeRepository.save(mode));
    }
}
