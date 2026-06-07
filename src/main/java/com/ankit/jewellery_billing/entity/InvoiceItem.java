package com.ankit.jewellery_billing.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "invoice_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id", nullable = false)
    private Invoice invoice;

    @Column(nullable = false, length = 150)
    private String itemName;

    @Column(nullable = false, precision = 10, scale = 3)
    private BigDecimal weight;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal ratePerGram;

    @Column(nullable = false, precision = 12, scale = 2)
    @Builder.Default
    private BigDecimal makingCharges = BigDecimal.ZERO;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal gstPercentage;

    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal itemTotal;
}
