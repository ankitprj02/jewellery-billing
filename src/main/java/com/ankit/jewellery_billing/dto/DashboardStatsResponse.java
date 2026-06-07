package com.ankit.jewellery_billing.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsResponse {
    private long totalCustomers;
    private long totalProducts;
    private long totalInvoices;
    private BigDecimal totalRevenue;
    private List<InvoiceResponse> recentInvoices;
    private Map<String, BigDecimal> monthlySales;
}
