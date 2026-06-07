package com.ankit.jewellery_billing.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ShopSettingsRequest {

    @NotBlank(message = "Shop name is required")
    @Size(max = 150, message = "Shop name must not exceed 150 characters")
    private String shopName;

    @Size(max = 500, message = "Address must not exceed 500 characters")
    private String address;

    @Size(max = 20, message = "Phone must not exceed 20 characters")
    private String phone;

    @Size(max = 100, message = "Email must not exceed 100 characters")
    private String email;

    @Size(max = 20, message = "GSTIN must not exceed 20 characters")
    private String gstin;

    @Size(max = 200, message = "Signature text must not exceed 200 characters")
    private String signatureText;
}
