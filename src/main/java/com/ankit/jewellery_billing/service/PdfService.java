package com.ankit.jewellery_billing.service;

import com.ankit.jewellery_billing.entity.Invoice;
import com.ankit.jewellery_billing.entity.InvoiceItem;
import com.ankit.jewellery_billing.entity.ShopSettings;
import com.ankit.jewellery_billing.exception.ResourceNotFoundException;
import com.ankit.jewellery_billing.repository.InvoiceRepository;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class PdfService {

    private final InvoiceRepository invoiceRepository;
    private final ShopSettingsService shopSettingsService;

    private static final Font TITLE_FONT = new Font(Font.HELVETICA, 18, Font.BOLD, new Color(139, 90, 43));
    private static final Font HEADER_FONT = new Font(Font.HELVETICA, 12, Font.BOLD);
    private static final Font NORMAL_FONT = new Font(Font.HELVETICA, 10, Font.NORMAL);
    private static final Font SMALL_FONT = new Font(Font.HELVETICA, 9, Font.NORMAL, Color.GRAY);

    public byte[] generateInvoicePdf(Long invoiceId) {
        Invoice invoice = invoiceRepository.findByIdWithDetails(invoiceId)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found with id: " + invoiceId));
        ShopSettings shop = shopSettingsService.getSettingsEntity();

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4, 40, 40, 40, 40);
            PdfWriter.getInstance(document, baos);
            document.open();

            // Shop header
            Paragraph shopName = new Paragraph(shop.getShopName(), TITLE_FONT);
            shopName.setAlignment(Element.ALIGN_CENTER);
            document.add(shopName);

            if (shop.getAddress() != null) {
                Paragraph address = new Paragraph(shop.getAddress(), SMALL_FONT);
                address.setAlignment(Element.ALIGN_CENTER);
                document.add(address);
            }

            StringBuilder contact = new StringBuilder();
            if (shop.getPhone() != null) contact.append("Phone: ").append(shop.getPhone());
            if (shop.getEmail() != null) {
                if (!contact.isEmpty()) contact.append(" | ");
                contact.append("Email: ").append(shop.getEmail());
            }
            if (shop.getGstin() != null) {
                if (!contact.isEmpty()) contact.append(" | ");
                contact.append("GSTIN: ").append(shop.getGstin());
            }
            if (!contact.isEmpty()) {
                Paragraph contactPara = new Paragraph(contact.toString(), SMALL_FONT);
                contactPara.setAlignment(Element.ALIGN_CENTER);
                contactPara.setSpacingAfter(15);
                document.add(contactPara);
            }

            // Invoice title
            Paragraph invoiceTitle = new Paragraph("TAX INVOICE", HEADER_FONT);
            invoiceTitle.setAlignment(Element.ALIGN_CENTER);
            invoiceTitle.setSpacingAfter(15);
            document.add(invoiceTitle);

            // Invoice & customer details
            PdfPTable infoTable = new PdfPTable(2);
            infoTable.setWidthPercentage(100);
            infoTable.setSpacingAfter(15);

            addInfoCell(infoTable, "Invoice No: " + invoice.getInvoiceNumber(), Element.ALIGN_LEFT);
            addInfoCell(infoTable, "Date: " + invoice.getInvoiceDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")), Element.ALIGN_RIGHT);

            addInfoCell(infoTable, "Customer: " + invoice.getCustomer().getName(), Element.ALIGN_LEFT);
            addInfoCell(infoTable, "Mobile: " + invoice.getCustomer().getMobileNumber(), Element.ALIGN_RIGHT);

            if (invoice.getCustomer().getAddress() != null) {
                PdfPCell addressCell = new PdfPCell(new Phrase("Address: " + invoice.getCustomer().getAddress(), NORMAL_FONT));
                addressCell.setColspan(2);
                addressCell.setBorder(Rectangle.NO_BORDER);
                addressCell.setPadding(3);
                infoTable.addCell(addressCell);
            }

            document.add(infoTable);

            // Items table
            PdfPTable itemsTable = new PdfPTable(new float[]{3, 1.2f, 1.5f, 1.5f, 1f, 1.5f});
            itemsTable.setWidthPercentage(100);
            itemsTable.setSpacingBefore(10);

            addHeaderCell(itemsTable, "Item Name");
            addHeaderCell(itemsTable, "Weight (g)");
            addHeaderCell(itemsTable, "Rate/g");
            addHeaderCell(itemsTable, "Making");
            addHeaderCell(itemsTable, "GST %");
            addHeaderCell(itemsTable, "Total");

            for (InvoiceItem item : invoice.getItems()) {
                addDataCell(itemsTable, item.getItemName());
                addDataCell(itemsTable, item.getWeight().toString());
                addDataCell(itemsTable, "₹" + item.getRatePerGram());
                addDataCell(itemsTable, "₹" + item.getMakingCharges());
                addDataCell(itemsTable, item.getGstPercentage() + "%");
                addDataCell(itemsTable, "₹" + item.getItemTotal());
            }

            document.add(itemsTable);

            // Totals
            PdfPTable totalsTable = new PdfPTable(2);
            totalsTable.setWidthPercentage(40);
            totalsTable.setHorizontalAlignment(Element.ALIGN_RIGHT);
            totalsTable.setSpacingBefore(15);

            addTotalRow(totalsTable, "Subtotal:", "₹" + invoice.getSubtotal());
            addTotalRow(totalsTable, "GST Amount:", "₹" + invoice.getGstAmount());
            addTotalRow(totalsTable, "Grand Total:", "₹" + invoice.getGrandTotal(), true);

            document.add(totalsTable);

            // Signature
            document.add(Chunk.NEWLINE);
            if (shop.getSignatureImage() != null && shop.getSignatureImage().length > 0) {
                Image signatureImage = Image.getInstance(shop.getSignatureImage());
                signatureImage.scaleToFit(140, 70);
                signatureImage.setAlignment(Element.ALIGN_RIGHT);
                signatureImage.setSpacingBefore(40);
                document.add(signatureImage);
            } else {
                Paragraph signLine = new Paragraph("_________________________", SMALL_FONT);
                signLine.setAlignment(Element.ALIGN_RIGHT);
                signLine.setSpacingBefore(50);
                document.add(signLine);
            }

            Paragraph signature = new Paragraph(
                    shop.getSignatureText() != null ? shop.getSignatureText() : "Authorized Signatory",
                    NORMAL_FONT
            );
            signature.setAlignment(Element.ALIGN_RIGHT);
            signature.setSpacingBefore(6);
            document.add(signature);

            document.close();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate PDF: " + e.getMessage(), e);
        }
    }

    private void addInfoCell(PdfPTable table, String text, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text, NORMAL_FONT));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(alignment);
        cell.setPadding(3);
        table.addCell(cell);
    }

    private void addHeaderCell(PdfPTable table, String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text, HEADER_FONT));
        cell.setBackgroundColor(new Color(245, 230, 211));
        cell.setPadding(8);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
    }

    private void addDataCell(PdfPTable table, String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text, NORMAL_FONT));
        cell.setPadding(6);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
    }

    private void addTotalRow(PdfPTable table, String label, String value) {
        addTotalRow(table, label, value, false);
    }

    private void addTotalRow(PdfPTable table, String label, String value, boolean bold) {
        Font font = bold ? HEADER_FONT : NORMAL_FONT;
        PdfPCell labelCell = new PdfPCell(new Phrase(label, font));
        labelCell.setBorder(Rectangle.NO_BORDER);
        labelCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        labelCell.setPadding(4);

        PdfPCell valueCell = new PdfPCell(new Phrase(value, font));
        valueCell.setBorder(Rectangle.NO_BORDER);
        valueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        valueCell.setPadding(4);

        table.addCell(labelCell);
        table.addCell(valueCell);
    }
}
