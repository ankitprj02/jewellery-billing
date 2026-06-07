package com.ankit.jewellery_billing.controller;

import com.ankit.jewellery_billing.dto.ApiResponse;
import com.ankit.jewellery_billing.dto.InvoiceRequest;
import com.ankit.jewellery_billing.dto.InvoiceResponse;
import com.ankit.jewellery_billing.service.InvoiceService;
import com.ankit.jewellery_billing.service.PdfService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;
    private final PdfService pdfService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<InvoiceResponse>>> getAll(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        List<InvoiceResponse> invoices;
        if (startDate != null && endDate != null) {
            invoices = invoiceService.filterByDateRange(startDate, endDate);
        } else if (search != null && !search.isBlank()) {
            invoices = invoiceService.searchByInvoiceNumber(search);
        } else {
            invoices = invoiceService.getAllInvoices();
        }
        return ResponseEntity.ok(ApiResponse.success(invoices));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<InvoiceResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(invoiceService.getInvoiceById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<InvoiceResponse>> create(@Valid @RequestBody InvoiceRequest request) {
        InvoiceResponse invoice = invoiceService.createInvoice(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Invoice created successfully", invoice));
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> downloadPdf(@PathVariable Long id) {
        byte[] pdf = pdfService.generateInvoicePdf(id);
        InvoiceResponse invoice = invoiceService.getInvoiceById(id);

        String filename = invoice.getInvoiceNumber() + ".pdf";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}
