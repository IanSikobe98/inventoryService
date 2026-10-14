package com.shoe.inventoryService.repository;

import com.shoe.inventoryService.entity.PaymentMode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentModeRepository extends JpaRepository<PaymentMode, Long> {

    Optional<PaymentMode> findByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCase(String name);

    List<PaymentMode> findByActiveTrue();
}
