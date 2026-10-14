package com.shoe.inventoryService.specification;

import com.shoe.inventoryService.entity.Sale;
import com.shoe.inventoryService.entity.ShoeBatch;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

/**
 * Composable filters for GET /api/sales.
 */
public final class SaleSpecifications {

    private SaleSpecifications() {
    }

    public static Specification<Sale> soldFrom(LocalDateTime from) {
        if (from == null) {
            return null;
        }
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("createdAt"), from);
    }

    public static Specification<Sale> soldTo(LocalDateTime to) {
        if (to == null) {
            return null;
        }
        return (root, query, cb) -> cb.lessThan(root.get("createdAt"), to);
    }

    public static Specification<Sale> paymentModeIdEquals(Long paymentModeId) {
        if (paymentModeId == null) {
            return null;
        }
        return (root, query, cb) -> cb.equal(root.get("paymentMode").get("id"), paymentModeId);
    }

    public static Specification<Sale> shoeBatchIdEquals(Long shoeBatchId) {
        if (shoeBatchId == null) {
            return null;
        }
        return (root, query, cb) -> cb.equal(root.get("shoeBatch").get("id"), shoeBatchId);
    }

    public static Specification<Sale> searchShoe(String text) {
        if (!StringUtils.hasText(text)) {
            return null;
        }
        String like = "%" + text.trim().toLowerCase() + "%";
        return (root, query, cb) -> {
            Join<Sale, ShoeBatch> batch = root.join("shoeBatch");
            return cb.or(
                    cb.like(cb.lower(batch.get("shoeCode")), like),
                    cb.like(cb.lower(batch.get("type")), like),
                    cb.like(cb.lower(batch.get("colour")), like)
            );
        };
    }
}
