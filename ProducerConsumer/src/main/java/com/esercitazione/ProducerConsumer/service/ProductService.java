package com.Esercitazione.ProducerConsumer.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.Esercitazione.ProducerConsumer.dto.ProductRequest;
import com.Esercitazione.ProducerConsumer.dto.ProductResponse;
import com.Esercitazione.ProducerConsumer.exception.ProductNotFoundException;
import com.Esercitazione.ProducerConsumer.model.Product;
import com.Esercitazione.ProducerConsumer.repository.ProductRepository;

@Service
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public List<ProductResponse> getAllProducts() {
        return repository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public ProductResponse getProductById(Long id) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        return toResponse(product);
    }

    public List<ProductResponse> searchProducts(
            Long id,
            String name,
            String category,
            String priceOrder) {

        String checkedName = StringUtils.hasText(name) ? name.trim() : null;
        String checkedCategory = StringUtils.hasText(category) ? category.trim() : null;

        Sort.Direction direction = Sort.Direction.ASC;

        if ("desc".equalsIgnoreCase(priceOrder)) {
            direction = Sort.Direction.DESC;
        }

        Pageable pageable = PageRequest.of(
                0,
                Integer.MAX_VALUE,
                Sort.by(direction, "price"));

        return repository.searchProducts(
                id,
                checkedName,
                checkedCategory,
                pageable)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public ProductResponse createProduct(ProductRequest request) {

        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("The product must have a name");
        }

        if (request.getPrice() == null) {
            throw new IllegalArgumentException("The product must have a price");
        }

        if (request.getCategory() == null || request.getCategory().trim().isEmpty()) {
            throw new IllegalArgumentException("The product must have a category");
        }

        Product product = new Product();

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setCategory(request.getCategory());

        if (request.getQuantity() != null) {
            product.setQuantity(request.getQuantity());
        }

        Product savedProduct = repository.save(product);

        return toResponse(savedProduct);
    }

    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest request) {

        Product updatingProduct = repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("The product must have a name");
        }

        if (request.getPrice() == null) {
            throw new IllegalArgumentException("The product must have a price");
        }

        if (request.getCategory() == null || request.getCategory().trim().isEmpty()) {
            throw new IllegalArgumentException("The product must have a category");
        }

        if (request.getQuantity() == null) {
            throw new IllegalArgumentException("The product must have a quantity");
        }

        updatingProduct.setName(request.getName());
        updatingProduct.setDescription(request.getDescription());
        updatingProduct.setPrice(request.getPrice());
        updatingProduct.setCategory(request.getCategory());
        updatingProduct.setQuantity(request.getQuantity());

        Product savedProduct = repository.save(updatingProduct);

        return toResponse(savedProduct);
    }

    @Transactional
    public void deleteProduct(Long id) {

        if (!repository.existsById(id)) {
            throw new ProductNotFoundException(id);
        }

        repository.deleteById(id);
    }

    private ProductResponse toResponse(Product product) {

        ProductResponse response = new ProductResponse();

        response.setId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setCategory(product.getCategory());
        response.setQuantity(product.getQuantity());
        response.setCreationDate(product.getCreationDate());

        return response;
    }
}