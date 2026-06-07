package com.ankit.jewellery_billing.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductRequest {

    @NotBlank(message = "Product name is required")
    @Size(max = 150, message = "Product name must not exceed 150 characters")
    private String productName;

    @NotBlank(message = "Category is required")
    @Size(max = 50, message = "Category must not exceed 50 characters")
    private String category;

    @NotNull(message = "Rate is required")
    @DecimalMin(value = "0.01", message = "Rate must be greater than 0")
    private BigDecimal currentRate;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;
}
