package com.ankit.jewellery_billing.repository;

import com.ankit.jewellery_billing.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE " +
           "LOWER(p.productName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(p.category) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<Product> search(@Param("search") String search);
}
