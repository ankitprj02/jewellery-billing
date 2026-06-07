package com.ankit.jewellery_billing.repository;

import com.ankit.jewellery_billing.entity.Invoice;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    Optional<Invoice> findByInvoiceNumber(String invoiceNumber);

    @Query("SELECT i FROM Invoice i JOIN FETCH i.customer JOIN FETCH i.items WHERE i.id = :id")
    Optional<Invoice> findByIdWithDetails(@Param("id") Long id);

    @Query("SELECT i FROM Invoice i JOIN FETCH i.customer ORDER BY i.createdAt DESC")
    List<Invoice> findAllWithCustomer();

    @Query("SELECT i FROM Invoice i JOIN FETCH i.customer WHERE " +
           "LOWER(i.invoiceNumber) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<Invoice> searchByInvoiceNumber(@Param("search") String search);

    @Query("SELECT i FROM Invoice i JOIN FETCH i.customer WHERE i.invoiceDate BETWEEN :startDate AND :endDate ORDER BY i.invoiceDate DESC")
    List<Invoice> findByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT COALESCE(SUM(i.grandTotal), 0) FROM Invoice i")
    BigDecimal getTotalRevenue();

    @Query("SELECT i FROM Invoice i JOIN FETCH i.customer ORDER BY i.createdAt DESC")
    List<Invoice> findRecentInvoices(Pageable pageable);

    @Query("SELECT COUNT(i) FROM Invoice i WHERE i.invoiceNumber LIKE CONCAT(:prefix, '%')")
    long countByInvoiceNumberPrefix(@Param("prefix") String prefix);

    @Query(value = "SELECT TO_CHAR(invoice_date, 'YYYY-MM') AS month, SUM(grand_total) AS total " +
           "FROM invoices WHERE invoice_date >= :startDate " +
           "GROUP BY TO_CHAR(invoice_date, 'YYYY-MM') ORDER BY month",
           nativeQuery = true)
    List<Object[]> getMonthlySales(@Param("startDate") LocalDate startDate);
}
