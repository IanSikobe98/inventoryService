package com.shoe.inventoryService.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
    name = "payment_modes",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_payment_mode_name", columnNames = "name")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentMode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true, length = 50)
    private String name;

    @Column(name = "active", nullable = false)
    @Builder.Default
    private Boolean active = true;

    @Column(name = "created_by", nullable = false)
    private Long createdBy;

    @Column(name = "updated_by", nullable = false)
    private Long updatedBy;

//    @OneToMany(
//        mappedBy = "paymentMode",
//        fetch = FetchType.LAZY
//    )
//    @Builder.Default
//    private List<Sale> sales = new ArrayList<>();
}