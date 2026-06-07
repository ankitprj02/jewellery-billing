package com.ankit.jewellery_billing.service;

import com.ankit.jewellery_billing.dto.ProductRequest;
import com.ankit.jewellery_billing.dto.ProductResponse;
import com.ankit.jewellery_billing.entity.Product;
import com.ankit.jewellery_billing.exception.ResourceNotFoundException;
import com.ankit.jewellery_billing.repository.ProductRepository;
import com.ankit.jewellery_billing.util.EntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream()
                .map(EntityMapper::toProductResponse)
                .toList();
    }

    public List<ProductResponse> searchProducts(String search) {
        return productRepository.search(search).stream()
                .map(EntityMapper::toProductResponse)
                .toList();
    }

    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        return EntityMapper.toProductResponse(product);
    }

    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        Product product = Product.builder()
                .productName(request.getProductName())
                .category(request.getCategory())
                .currentRate(request.getCurrentRate())
                .description(request.getDescription())
                .build();
        return EntityMapper.toProductResponse(productRepository.save(product));
    }

    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        product.setProductName(request.getProductName());
        product.setCategory(request.getCategory());
        product.setCurrentRate(request.getCurrentRate());
        product.setDescription(request.getDescription());
        return EntityMapper.toProductResponse(productRepository.save(product));
    }

    @Transactional
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }
}
