package com.ankit.jewellery_billing.service;

import com.ankit.jewellery_billing.dto.InvoiceItemRequest;
import com.ankit.jewellery_billing.dto.InvoiceRequest;
import com.ankit.jewellery_billing.dto.InvoiceResponse;
import com.ankit.jewellery_billing.entity.Customer;
import com.ankit.jewellery_billing.entity.Invoice;
import com.ankit.jewellery_billing.entity.InvoiceItem;
import com.ankit.jewellery_billing.exception.BadRequestException;
import com.ankit.jewellery_billing.exception.ResourceNotFoundException;
import com.ankit.jewellery_billing.repository.CustomerRepository;
import com.ankit.jewellery_billing.repository.InvoiceRepository;
import com.ankit.jewellery_billing.util.EntityMapper;
import com.ankit.jewellery_billing.util.InvoiceCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final CustomerRepository customerRepository;

    public List<InvoiceResponse> getAllInvoices() {
        return invoiceRepository.findAllWithCustomer().stream()
                .map(EntityMapper::toInvoiceResponse)
                .toList();
    }

    public List<InvoiceResponse> searchByInvoiceNumber(String search) {
        return invoiceRepository.searchByInvoiceNumber(search).stream()
                .map(EntityMapper::toInvoiceResponse)
                .toList();
    }

    public List<InvoiceResponse> filterByDateRange(LocalDate startDate, LocalDate endDate) {
        return invoiceRepository.findByDateRange(startDate, endDate).stream()
                .map(EntityMapper::toInvoiceResponse)
                .toList();
    }

    public InvoiceResponse getInvoiceById(Long id) {
        Invoice invoice = invoiceRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found with id: " + id));
        return EntityMapper.toInvoiceResponse(invoice);
    }

    @Transactional
    public InvoiceResponse createInvoice(InvoiceRequest request) {
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + request.getCustomerId()));

        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new BadRequestException("At least one invoice item is required");
        }

        String invoiceNumber = generateInvoiceNumber(request.getInvoiceDate());
        List<InvoiceItem> items = new ArrayList<>();
        BigDecimal totalSubtotal = BigDecimal.ZERO;
        BigDecimal totalGst = BigDecimal.ZERO;

        for (InvoiceItemRequest itemRequest : request.getItems()) {
            BigDecimal itemSubtotal = InvoiceCalculator.calculateItemSubtotal(itemRequest);
            BigDecimal itemGst = InvoiceCalculator.calculateItemGst(itemRequest);
            BigDecimal itemTotal = InvoiceCalculator.calculateItemTotal(itemRequest);

            totalSubtotal = totalSubtotal.add(itemSubtotal);
            totalGst = totalGst.add(itemGst);

            InvoiceItem item = InvoiceItem.builder()
                    .itemName(itemRequest.getItemName())
                    .weight(itemRequest.getWeight())
                    .ratePerGram(itemRequest.getRatePerGram())
                    .makingCharges(itemRequest.getMakingCharges() != null ? itemRequest.getMakingCharges() : BigDecimal.ZERO)
                    .gstPercentage(itemRequest.getGstPercentage())
                    .itemTotal(itemTotal)
                    .build();
            items.add(item);
        }

        BigDecimal grandTotal = totalSubtotal.add(totalGst);

        Invoice invoice = Invoice.builder()
                .invoiceNumber(invoiceNumber)
                .customer(customer)
                .invoiceDate(request.getInvoiceDate())
                .subtotal(totalSubtotal)
                .gstAmount(totalGst)
                .grandTotal(grandTotal)
                .items(new ArrayList<>())
                .build();

        for (InvoiceItem item : items) {
            item.setInvoice(invoice);
            invoice.getItems().add(item);
        }

        Invoice saved = invoiceRepository.save(invoice);
        return EntityMapper.toInvoiceResponse(
                invoiceRepository.findByIdWithDetails(saved.getId()).orElse(saved)
        );
    }

    private String generateInvoiceNumber(LocalDate date) {
        String datePart = date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String prefix = "INV-" + datePart + "-";
        long count = invoiceRepository.countByInvoiceNumberPrefix(prefix);
        return prefix + String.format("%04d", count + 1);
    }
}
