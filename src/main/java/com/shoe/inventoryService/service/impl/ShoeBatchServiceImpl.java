package com.shoe.inventoryService.service.impl;

import com.shoe.inventoryService.dto.request.RestockRequest;
import com.shoe.inventoryService.dto.request.ShoeBatchCreateRequest;
import com.shoe.inventoryService.dto.request.ShoeBatchUpdateRequest;
import com.shoe.inventoryService.dto.response.ShoeBatchResponse;
import com.shoe.inventoryService.entity.ShoeBatch;
import com.shoe.inventoryService.exception.DuplicateResourceException;
import com.shoe.inventoryService.exception.ResourceNotFoundException;
import com.shoe.inventoryService.mapper.ShoeBatchMapper;
import com.shoe.inventoryService.repository.ShoeBatchRepository;
import com.shoe.inventoryService.service.ShoeBatchService;
import com.shoe.inventoryService.specification.ShoeBatchSpecifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ShoeBatchServiceImpl implements ShoeBatchService {

    private final ShoeBatchRepository shoeBatchRepository;


    public ShoeBatchServiceImpl(ShoeBatchRepository shoeBatchRepository) {
        this.shoeBatchRepository = shoeBatchRepository;
//        this.currentUserProvider = currentUserProvider;
    }

    @Override
    public ShoeBatchResponse create(ShoeBatchCreateRequest request) {
        if (shoeBatchRepository.existsByShoeCodeIgnoreCase(request.shoeCode())) {
            throw new DuplicateResourceException("A shoe batch with code '" + request.shoeCode() + "' already exists");
        }
        Long userId = 2L;
        ShoeBatch batch = ShoeBatch.builder()
                .shoeCode(request.shoeCode().trim())
                .size(request.size())
                .type(request.type())
                .colour(request.colour())
                .pictureUrl(request.pictureUrl())
                .quantityReceived(request.quantityReceived())
                .quantityRemaining(request.quantityReceived())
                .costPerShoe(request.costPerShoe())
                .wholesalePrice(request.wholesalePrice())
                .retailPrice(request.retailPrice())
                .createdBy(userId)
                .updatedBy(userId)
                .build();
        return ShoeBatchMapper.toResponse(shoeBatchRepository.save(batch));
    }

    @Override
    public ShoeBatchResponse update(Long id, ShoeBatchUpdateRequest request) {
        ShoeBatch batch = shoeBatchRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.forEntity("ShoeBatch", id));

        if (shoeBatchRepository.existsByShoeCodeIgnoreCaseAndIdNot(request.shoeCode(), id)) {
            throw new DuplicateResourceException("A shoe batch with code '" + request.shoeCode() + "' already exists");
        }

        batch.setShoeCode(request.shoeCode().trim());
        batch.setSize(request.size());
        batch.setType(request.type());
        batch.setColour(request.colour());
        batch.setPictureUrl(request.pictureUrl());
        batch.setCostPerShoe(request.costPerShoe());
        batch.setWholesalePrice(request.wholesalePrice());
        batch.setRetailPrice(request.retailPrice());
        batch.setUpdatedBy(2L);
        // quantityReceived / quantityRemaining are untouched here — see restock().

        return ShoeBatchMapper.toResponse(shoeBatchRepository.save(batch));
    }

    @Override
    public ShoeBatchResponse restock(Long id, RestockRequest request) {
        // Row-locked so a restock and a concurrent sale can't both read a
        // stale quantityRemaining and overwrite each other's change.
        ShoeBatch batch = shoeBatchRepository.findByIdForUpdate(id)
                .orElseThrow(() -> ResourceNotFoundException.forEntity("ShoeBatch", id));

        int add = request.quantity();
        batch.setQuantityReceived(batch.getQuantityReceived() + add);
        batch.setQuantityRemaining(batch.getQuantityRemaining() + add);
        batch.setUpdatedBy(2L);

        return ShoeBatchMapper.toResponse(shoeBatchRepository.save(batch));
    }

    @Override
    @Transactional(readOnly = true)
    public ShoeBatchResponse getById(Long id) {
        return shoeBatchRepository.findById(id)
                .map(ShoeBatchMapper::toResponse)
                .orElseThrow(() -> ResourceNotFoundException.forEntity("ShoeBatch", id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ShoeBatchResponse> search(String query, String size,String shoeSize, boolean lowStockOnly,
                                           boolean outOfStockOnly, Pageable pageable) {
        List<Specification<ShoeBatch>> specs = new ArrayList<>();
        addIfPresent(specs, ShoeBatchSpecifications.search(query));
        addIfPresent(specs, ShoeBatchSpecifications.sizeEquals(shoeSize));
        addIfPresent(specs, ShoeBatchSpecifications.lowStockOnly(lowStockOnly, ShoeBatchMapper.LOW_STOCK_THRESHOLD));
        addIfPresent(specs, ShoeBatchSpecifications.outOfStockOnly(outOfStockOnly));

        Specification<ShoeBatch> combined = specs.stream().reduce(Specification::and).orElse(null);
        return shoeBatchRepository.findAll(combined, pageable).map(ShoeBatchMapper::toResponse);
    }

    @Override
    public void delete(Long id) {
        if (!shoeBatchRepository.existsById(id)) {
            throw ResourceNotFoundException.forEntity("ShoeBatch", id);
        }
        shoeBatchRepository.deleteById(id);
    }

    private void addIfPresent(List<Specification<ShoeBatch>> specs, Specification<ShoeBatch> spec) {
        if (spec != null) {
            specs.add(spec);
        }
    }
}
