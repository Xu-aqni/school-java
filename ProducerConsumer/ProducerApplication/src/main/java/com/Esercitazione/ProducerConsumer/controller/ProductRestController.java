package com.Esercitazione.ProducerConsumer.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.Esercitazione.ProducerConsumer.dto.ProductRequest;
import com.Esercitazione.ProducerConsumer.dto.ProductResponse;
import com.Esercitazione.ProducerConsumer.service.ProductService;

@RestController
@RequestMapping("/api/products")
public class ProductRestController {

    private final ProductService service;

    public ProductRestController(ProductService service) {
        this.service = service;
    }

    // GET /api/products
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        return ResponseEntity.ok(service.getAllProducts());
    }

    // GET /api/products/1
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {

        ProductResponse product = service.getProductById(id);

        return ResponseEntity.ok(product);
    }

    // GET /api/products/search?name=Laptop&category=Computers&priceOrder=desc
    @GetMapping("/search")
    public ResponseEntity<List<ProductResponse>> searchProducts(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String priceOrder) {

        return ResponseEntity.ok(
                service.searchProducts(
                        id,
                        name,
                        category,
                        priceOrder));
    }

    // POST /api/products
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(
            @RequestBody ProductRequest request) {

        ProductResponse createdProduct = service.createProduct(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdProduct);
    }

    // PUT /api/products/1
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long id,
            @RequestBody ProductRequest request) {

        ProductResponse updatedProduct = service.updateProduct(id, request);

        return ResponseEntity.ok(updatedProduct);
    }

    // DELETE /api/products/1
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable Long id) {

        service.deleteProduct(id);
    }
}
