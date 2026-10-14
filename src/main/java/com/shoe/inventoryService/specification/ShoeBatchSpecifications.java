package com.shoe.inventoryService.specification;

import com.shoe.inventoryService.entity.ShoeBatch;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

/**
 * Composable filters for GET /api/shoe-batches. Each method returns null
 * when the filter wasn't supplied, so callers can chain them with
 * Specification.allOf(...) / .and(...) without null-checking every time.
 */
public final class ShoeBatchSpecifications {

    private ShoeBatchSpecifications() {
    }

    public static Specification<ShoeBatch> search(String text) {
        if (!StringUtils.hasText(text)) {
            return null;
        }
        String like = "%" + text.trim().toLowerCase() + "%";
        return (root, query, cb) -> cb.or(
                cb.like(cb.lower(root.get("shoeCode")), like),
                cb.like(cb.lower(root.get("type")), like),
                cb.like(cb.lower(root.get("colour")), like)
        );
    }

    public static Specification<ShoeBatch> sizeEquals(String size) {
        if (!StringUtils.hasText(size)) {
            return null;
        }
        return (root, query, cb) -> cb.equal(root.get("size"), size);
    }

    public static Specification<ShoeBatch> lowStockOnly(boolean lowOnly, int threshold) {
        if (!lowOnly) {
            return null;
        }
        return (root, query, cb) -> cb.and(
                cb.greaterThan(root.get("quantityRemaining"), 0),
                cb.lessThanOrEqualTo(root.get("quantityRemaining"), threshold)
        );
    }

    public static Specification<ShoeBatch> outOfStockOnly(boolean outOfStockOnly) {
        if (!outOfStockOnly) {
            return null;
        }
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("quantityRemaining"), 0);
    }
}
