package com.shoe.inventoryService.repository;

import com.shoe.inventoryService.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface SaleRepository extends JpaRepository<Sale, Long>,
        JpaSpecificationExecutor<Sale> {

    List<Sale> findByShoeBatchIdOrderByCreatedAtDesc(Long shoeBatchId);

    @Query("select s from Sale s where s.createdAt >= :from and s.createdAt < :to")
    List<Sale> findBetween(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);
}
