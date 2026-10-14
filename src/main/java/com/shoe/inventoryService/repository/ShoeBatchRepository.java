package com.shoe.inventoryService.repository;

import com.shoe.inventoryService.entity.ShoeBatch;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ShoeBatchRepository extends JpaRepository<ShoeBatch, Long>,
        JpaSpecificationExecutor<ShoeBatch> {

    Optional<ShoeBatch> findByShoeCodeIgnoreCase(String shoeCode);

    boolean existsByShoeCodeIgnoreCase(String shoeCode);

    boolean existsByShoeCodeIgnoreCaseAndIdNot(String shoeCode, Long id);

    /**
     * Locks the row for the duration of the transaction so two concurrent
     * sales (or a sale and a restock) can't both read the same
     * quantityRemaining and decrement/increment from a stale value.
     * Always call this inside a @Transactional method and keep the
     * transaction short.
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select b from ShoeBatch b where b.id = :id")
    Optional<ShoeBatch> findByIdForUpdate(@Param("id") Long id);

    @Query("select b from ShoeBatch b where b.quantityRemaining <= :threshold")
    List<ShoeBatch> findLowStock(@Param("threshold") int threshold);
}
