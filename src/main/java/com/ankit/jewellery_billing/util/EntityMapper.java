package com.ankit.jewellery_billing.util;

import com.ankit.jewellery_billing.dto.*;
import com.ankit.jewellery_billing.entity.*;

import java.util.List;
import java.util.stream.Collectors;

public final class EntityMapper {

    private EntityMapper() {}

    public static CustomerResponse toCustomerResponse(Customer customer) {
        return CustomerResponse.builder()
                .id(customer.getId())
                .name(customer.getName())
                .mobileNumber(customer.getMobileNumber())
                .address(customer.getAddress())
                .createdDate(customer.getCreatedDate())
                .build();
    }

    public static ProductResponse toProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .productName(product.getProductName())
                .category(product.getCategory())
                .currentRate(product.getCurrentRate())
                .description(product.getDescription())
                .build();
    }

    public static InvoiceItemResponse toInvoiceItemResponse(InvoiceItem item) {
        return InvoiceItemResponse.builder()
                .id(item.getId())
                .itemName(item.getItemName())
                .weight(item.getWeight())
                .ratePerGram(item.getRatePerGram())
                .makingCharges(item.getMakingCharges())
                .gstPercentage(item.getGstPercentage())
                .itemTotal(item.getItemTotal())
                .build();
    }

    public static InvoiceResponse toInvoiceResponse(Invoice invoice) {
        List<InvoiceItemResponse> items = invoice.getItems() != null
                ? invoice.getItems().stream().map(EntityMapper::toInvoiceItemResponse).collect(Collectors.toList())
                : List.of();

        return InvoiceResponse.builder()
                .id(invoice.getId())
                .invoiceNumber(invoice.getInvoiceNumber())
                .customer(toCustomerResponse(invoice.getCustomer()))
                .invoiceDate(invoice.getInvoiceDate())
                .items(items)
                .subtotal(invoice.getSubtotal())
                .gstAmount(invoice.getGstAmount())
                .grandTotal(invoice.getGrandTotal())
                .createdAt(invoice.getCreatedAt())
                .build();
    }

    public static ShopSettingsResponse toShopSettingsResponse(ShopSettings settings) {
        return ShopSettingsResponse.builder()
                .id(settings.getId())
                .shopName(settings.getShopName())
                .address(settings.getAddress())
                .phone(settings.getPhone())
                .email(settings.getEmail())
                .gstin(settings.getGstin())
                .signatureText(settings.getSignatureText())
                .hasSignatureImage(settings.getSignatureImage() != null && settings.getSignatureImage().length > 0)
                .build();
    }
}
