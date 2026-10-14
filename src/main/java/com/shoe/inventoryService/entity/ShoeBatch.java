package com.shoe.inventoryService.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
    name = "shoe_batches",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_shoe_code", columnNames = "shoe_code")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShoeBatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "shoe_code", nullable = false, unique = true, length = 50)
    private String shoeCode;

    @Column(name = "size", nullable = false, length = 20)
    private String size;

    @Column(name = "type", nullable = false, length = 100)
    private String type;

    @Column(name = "colour", nullable = false, length = 50)
    private String colour;

    @Column(name = "picture_url")
    private String pictureUrl;

    @Column(name = "quantity_received", nullable = false)
    private Integer quantityReceived;

    @Column(name = "quantity_remaining", nullable = false)
    private Integer quantityRemaining;

    @Column(name = "cost_per_shoe", nullable = false, precision = 12, scale = 2)
    private BigDecimal costPerShoe;

    @Column(name = "wholesale_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal wholesalePrice;

    @Column(name = "retail_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal retailPrice;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "created_by", nullable = false)
    private Long createdBy;

    @Column(name = "updated_by", nullable = false)
    private Long updatedBy;

//    @OneToMany(
//        mappedBy = "shoeBatch",
//        fetch = FetchType.LAZY
//    )
//    @Builder.Default
//    private List<Sale> sales = new ArrayList<>();

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