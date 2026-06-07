package com.ankit.jewellery_billing.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceItemResponse {
    private Long id;
    private String itemName;
    private BigDecimal weight;
    private BigDecimal ratePerGram;
    private BigDecimal makingCharges;
    private BigDecimal gstPercentage;
    private BigDecimal itemTotal;
}
