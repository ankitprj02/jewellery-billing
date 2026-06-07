package com.ankit.jewellery_billing.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShopSettingsResponse {
    private Long id;
    private String shopName;
    private String address;
    private String phone;
    private String email;
    private String gstin;
    private String signatureText;
    private boolean hasSignatureImage;
}
