package com.ankit.jewellery_billing.repository;

import com.ankit.jewellery_billing.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query("SELECT c FROM Customer c WHERE " +
           "LOWER(c.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "c.mobileNumber LIKE CONCAT('%', :search, '%')")
    List<Customer> search(@Param("search") String search);
}
