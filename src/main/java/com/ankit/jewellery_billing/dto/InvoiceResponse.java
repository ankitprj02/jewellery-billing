package com.ankit.jewellery_billing.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceResponse {
    private Long id;
    private String invoiceNumber;
    private CustomerResponse customer;
    private LocalDate invoiceDate;
    private List<InvoiceItemResponse> items;
    private BigDecimal subtotal;
    private BigDecimal gstAmount;
    private BigDecimal grandTotal;
    private LocalDateTime createdAt;
}
