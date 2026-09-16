package com.shoe.inventoryService.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "sales")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "shoe_batch_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_sale_shoe_batch")
    )
    private ShoeBatch shoeBatch;

    @Column(
        name = "actual_selling_price",
        nullable = false,
        precision = 12,
        scale = 2
    )
    private BigDecimal actualSellingPrice;

    @Column(name = "quantity_sold", nullable = false)
    private Integer quantitySold;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "payment_mode_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_sale_payment_mode")
    )
    private PaymentMode paymentMode;

    @Column(
        name = "total_amount",
        nullable = false,
        precision = 14,
        scale = 2
    )
    private BigDecimal totalAmount;

    @Column(name = "remarks", length = 1000)
    private String remarks;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "created_by", nullable = false)
    private Long createdBy;

    @Column(name = "updated_by", nullable = false)
    private Long updatedBy;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();

        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}