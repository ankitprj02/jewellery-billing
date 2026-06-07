package com.ankit.jewellery_billing.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class InvoiceRequest {

    @NotNull(message = "Customer ID is required")
    private Long customerId;

    @NotNull(message = "Invoice date is required")
    private LocalDate invoiceDate;

    @NotEmpty(message = "At least one item is required")
    @Valid
    private List<InvoiceItemRequest> items;
}
