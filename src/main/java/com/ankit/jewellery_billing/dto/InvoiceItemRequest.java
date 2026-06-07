package com.ankit.jewellery_billing.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class InvoiceItemRequest {

    @NotBlank(message = "Item name is required")
    @Size(max = 150, message = "Item name must not exceed 150 characters")
    private String itemName;

    @NotNull(message = "Weight is required")
    @DecimalMin(value = "0.001", message = "Weight must be greater than 0")
    private BigDecimal weight;

    @NotNull(message = "Rate per gram is required")
    @DecimalMin(value = "0.01", message = "Rate must be greater than 0")
    private BigDecimal ratePerGram;

    @DecimalMin(value = "0.0", message = "Making charges cannot be negative")
    private BigDecimal makingCharges;

    @NotNull(message = "GST percentage is required")
    @DecimalMin(value = "0.0", message = "GST percentage cannot be negative")
    private BigDecimal gstPercentage;
}
