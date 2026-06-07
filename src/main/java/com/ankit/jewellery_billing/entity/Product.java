package com.ankit.jewellery_billing.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String productName;

    @Column(nullable = false, length = 50)
    private String category;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal currentRate;

    @Column(length = 1000)
    private String description;
}
