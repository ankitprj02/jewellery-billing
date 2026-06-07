package com.ankit.jewellery_billing.service;

import com.ankit.jewellery_billing.dto.DashboardStatsResponse;
import com.ankit.jewellery_billing.dto.InvoiceResponse;
import com.ankit.jewellery_billing.repository.CustomerRepository;
import com.ankit.jewellery_billing.repository.InvoiceRepository;
import com.ankit.jewellery_billing.repository.ProductRepository;
import com.ankit.jewellery_billing.util.EntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final InvoiceRepository invoiceRepository;

    public DashboardStatsResponse getDashboardStats() {
        List<InvoiceResponse> recentInvoices = invoiceRepository.findRecentInvoices(PageRequest.of(0, 5)).stream()
                .map(EntityMapper::toInvoiceResponse)
                .toList();

        Map<String, BigDecimal> monthlySales = new LinkedHashMap<>();
        LocalDate sixMonthsAgo = LocalDate.now().minusMonths(6);
        List<Object[]> salesData = invoiceRepository.getMonthlySales(sixMonthsAgo);
        for (Object[] row : salesData) {
            monthlySales.put((String) row[0], (BigDecimal) row[1]);
        }

        return DashboardStatsResponse.builder()
                .totalCustomers(customerRepository.count())
                .totalProducts(productRepository.count())
                .totalInvoices(invoiceRepository.count())
                .totalRevenue(invoiceRepository.getTotalRevenue())
                .recentInvoices(recentInvoices)
                .monthlySales(monthlySales)
                .build();
    }
}
