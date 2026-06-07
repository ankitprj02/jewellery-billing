package com.ankit.jewellery_billing.service;

import com.ankit.jewellery_billing.dto.CustomerRequest;
import com.ankit.jewellery_billing.dto.CustomerResponse;
import com.ankit.jewellery_billing.entity.Customer;
import com.ankit.jewellery_billing.exception.ResourceNotFoundException;
import com.ankit.jewellery_billing.repository.CustomerRepository;
import com.ankit.jewellery_billing.util.EntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public List<CustomerResponse> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(EntityMapper::toCustomerResponse)
                .toList();
    }

    public List<CustomerResponse> searchCustomers(String search) {
        return customerRepository.search(search).stream()
                .map(EntityMapper::toCustomerResponse)
                .toList();
    }

    public CustomerResponse getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
        return EntityMapper.toCustomerResponse(customer);
    }

    @Transactional
    public CustomerResponse createCustomer(CustomerRequest request) {
        Customer customer = Customer.builder()
                .name(request.getName())
                .mobileNumber(request.getMobileNumber())
                .address(request.getAddress())
                .build();
        return EntityMapper.toCustomerResponse(customerRepository.save(customer));
    }

    @Transactional
    public CustomerResponse updateCustomer(Long id, CustomerRequest request) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
        customer.setName(request.getName());
        customer.setMobileNumber(request.getMobileNumber());
        customer.setAddress(request.getAddress());
        return EntityMapper.toCustomerResponse(customerRepository.save(customer));
    }

    @Transactional
    public void deleteCustomer(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Customer not found with id: " + id);
        }
        customerRepository.deleteById(id);
    }
}
