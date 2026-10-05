package com.Esercitazione.ProducerConsumer.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Esercitazione.ProducerConsumer.model.Product;
import com.Esercitazione.ProducerConsumer.repository.ProductRepository;

@Service
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public List<Product> getAllProducts() {
        return repository.findAll();
    }

    public Product getProductById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product with id: '" + id + "' not found"));
    }

    public List<Product> getProductByCategory(String category) {
        return repository.findByCategory(category);
    }

    public List<Product> getProductByName(String name) {
        return repository.findByName(name);
    }

    public List<Product> getAllProductByOrderByPriceDesc() {
        return repository.findAllByOrderByPriceDesc();
    }

    @Transactional
    public Product createProduct(Product product) {
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("The product must have a name");
        }
        if (product.getPrice() == null) {
            throw new IllegalArgumentException("The product must have a price");
        }
        if (product.getCategory() == null || product.getCategory().trim().isEmpty()) {
            throw new IllegalArgumentException("The product must have a category");
        }

        Product savedProduct = repository.save(product);

        return savedProduct;
    }

    @Transactional
    public Product updateProduct(Long id, Product newProduct) {
        Product updatingProduct = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product with id: '" + id + "' not found"));

        if (newProduct.getName() == null || newProduct.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("The product must have a name");
        }
        if (newProduct.getPrice() == null) {
            throw new IllegalArgumentException("The product must have a price");
        }
        if (newProduct.getCategory() == null || newProduct.getCategory().trim().isEmpty()) {
            throw new IllegalArgumentException("The product must have a category");
        }
        if (newProduct.getQuantity() == null) {
            throw new IllegalArgumentException("The product must have a quantity");
        }

        updatingProduct.setName(newProduct.getName());
        updatingProduct.setDescription(newProduct.getDescription());
        updatingProduct.setPrice(newProduct.getPrice());
        updatingProduct.setCategory(newProduct.getCategory());
        updatingProduct.setQuantity(newProduct.getQuantity());
        

        return repository.save(updatingProduct);
    }

    @Transactional
    public void deleteProduct(Long id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Product with id: '" + id + "' not found");
        }

        repository.deleteById(id);
    }
}
