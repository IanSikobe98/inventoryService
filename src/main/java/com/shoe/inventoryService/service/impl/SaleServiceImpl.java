package com.shoe.inventoryService.service.impl;

import com.shoe.inventoryService.dto.request.SaleCreateRequest;
import com.shoe.inventoryService.dto.response.SaleResponse;
import com.shoe.inventoryService.entity.PaymentMode;
import com.shoe.inventoryService.entity.Sale;
import com.shoe.inventoryService.entity.ShoeBatch;
import com.shoe.inventoryService.exception.InsufficientStockException;
import com.shoe.inventoryService.exception.ResourceNotFoundException;
import com.shoe.inventoryService.mapper.SaleMapper;
import com.shoe.inventoryService.repository.PaymentModeRepository;
import com.shoe.inventoryService.repository.SaleRepository;
import com.shoe.inventoryService.repository.ShoeBatchRepository;
import com.shoe.inventoryService.service.SaleService;
import com.shoe.inventoryService.specification.SaleSpecifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class SaleServiceImpl implements SaleService {

    private final SaleRepository saleRepository;
    private final ShoeBatchRepository shoeBatchRepository;
    private final PaymentModeRepository paymentModeRepository;

    public SaleServiceImpl(SaleRepository saleRepository,
                            ShoeBatchRepository shoeBatchRepository,
                            PaymentModeRepository paymentModeRepository
                           ) {
        this.saleRepository = saleRepository;
        this.shoeBatchRepository = shoeBatchRepository;
        this.paymentModeRepository = paymentModeRepository;
    }

    @Override
    public SaleResponse create(SaleCreateRequest request) {
        // Row-locked read: holds a DB-level write lock on this shoe batch
        // until the transaction commits, so two sales submitted at the same
        // moment are serialized and can't both succeed against the same
        // last few pairs.
        ShoeBatch batch = shoeBatchRepository.findByIdForUpdate(request.shoeBatchId())
                .orElseThrow(() -> ResourceNotFoundException.forEntity("ShoeBatch", request.shoeBatchId()));

        if (request.quantitySold() > batch.getQuantityRemaining()) {
            throw new InsufficientStockException(
                    "Only " + batch.getQuantityRemaining() + " pair(s) of " + batch.getShoeCode() + " remain"
            );
        }

        PaymentMode mode = paymentModeRepository.findById(request.paymentModeId())
                .orElseThrow(() -> ResourceNotFoundException.forEntity("PaymentMode", request.paymentModeId()));

        batch.setQuantityRemaining(batch.getQuantityRemaining() - request.quantitySold());
        batch.setUpdatedBy(2L);
        shoeBatchRepository.save(batch);

        Long userId = 2L;
        Sale sale = Sale.builder()
                .shoeBatch(batch)
                .actualSellingPrice(request.actualSellingPrice())
                .quantitySold(request.quantitySold())
                .paymentMode(mode)
                .totalAmount(request.actualSellingPrice().multiply(java.math.BigDecimal.valueOf(request.quantitySold())))
                .remarks(request.remarks())
                .createdBy(userId)
                .updatedBy(userId)
                .build();

        return SaleMapper.toResponse(saleRepository.save(sale));
    }

    @Override
    @Transactional(readOnly = true)
    public SaleResponse getById(Long id) {
        return saleRepository.findById(id)
                .map(SaleMapper::toResponse)
                .orElseThrow(() -> ResourceNotFoundException.forEntity("Sale", id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SaleResponse> search(LocalDate from, LocalDate to, Long paymentModeId,
                                      Long shoeBatchId, String query, Pageable pageable) {
        LocalDateTime fromDateTime = from == null ? null : from.atStartOfDay();
        LocalDateTime toDateTime = to == null ? null : to.plusDays(1).atStartOfDay();

        List<Specification<Sale>> specs = new ArrayList<>();
        addIfPresent(specs, SaleSpecifications.soldFrom(fromDateTime));
        addIfPresent(specs, SaleSpecifications.soldTo(toDateTime));
        addIfPresent(specs, SaleSpecifications.paymentModeIdEquals(paymentModeId));
        addIfPresent(specs, SaleSpecifications.shoeBatchIdEquals(shoeBatchId));
        addIfPresent(specs, SaleSpecifications.searchShoe(query));

        Specification<Sale> combined = specs.stream().reduce(Specification::and).orElse(null);
        return saleRepository.findAll(combined, pageable).map(SaleMapper::toResponse);
    }

    private void addIfPresent(List<Specification<Sale>> specs, Specification<Sale> spec) {
        if (spec != null) {
            specs.add(spec);
        }
    }
}
