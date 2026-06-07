package com.ankit.jewellery_billing.util;

import com.ankit.jewellery_billing.dto.InvoiceItemRequest;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Utility for invoice item and total calculations.
 * Base Amount = Weight × Rate
 * Subtotal = Base Amount + Making Charges
 * GST Amount = Subtotal × GST / 100
 * Item Total = Subtotal + GST Amount
 */
public final class InvoiceCalculator {

    private InvoiceCalculator() {}

    public static BigDecimal calculateItemTotal(InvoiceItemRequest item) {
        BigDecimal weight = item.getWeight();
        BigDecimal rate = item.getRatePerGram();
        BigDecimal makingCharges = item.getMakingCharges() != null ? item.getMakingCharges() : BigDecimal.ZERO;
        BigDecimal gstPercentage = item.getGstPercentage();

        BigDecimal baseAmount = weight.multiply(rate).setScale(2, RoundingMode.HALF_UP);
        BigDecimal subtotal = baseAmount.add(makingCharges).setScale(2, RoundingMode.HALF_UP);
        BigDecimal gstAmount = subtotal.multiply(gstPercentage)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

        return subtotal.add(gstAmount).setScale(2, RoundingMode.HALF_UP);
    }

    public static BigDecimal calculateItemSubtotal(InvoiceItemRequest item) {
        BigDecimal baseAmount = item.getWeight().multiply(item.getRatePerGram()).setScale(2, RoundingMode.HALF_UP);
        BigDecimal makingCharges = item.getMakingCharges() != null ? item.getMakingCharges() : BigDecimal.ZERO;
        return baseAmount.add(makingCharges).setScale(2, RoundingMode.HALF_UP);
    }

    public static BigDecimal calculateItemGst(InvoiceItemRequest item) {
        BigDecimal subtotal = calculateItemSubtotal(item);
        return subtotal.multiply(item.getGstPercentage())
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }
}
