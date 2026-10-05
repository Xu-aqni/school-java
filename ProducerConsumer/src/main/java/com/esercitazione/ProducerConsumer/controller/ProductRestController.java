package com.Esercitazione.ProducerConsumer.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Esercitazione.ProducerConsumer.model.Product;
import com.Esercitazione.ProducerConsumer.service.ProductService;

@RestController 
@RequestMapping("/api/products")
public class ProductRestController {
    
    private final ProductService service;
    
    public ProductRestController(ProductService service){
        this.service = service;
    }

    @GetMapping
    public List<Product> getAllProducts(){
        return service.getAllProducts();
    }
    
    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id){
        return service.getProductById(id);
    }

    @GetMapping("/{category}")
    public List<Product> getProductByCategory(@PathVariable String category){
        return service.getProductByCategory(category);
    }

    @GetMapping("/{name}")
    public List<Product> getProductByName(@PathVariable String name){
        return service.getProductByName(name);
    }

    public List<Product> getAllProductByOrderByPriceDesc() {
        return service.getAllProductByOrderByPriceDesc();
    }

    @PostMapping 
    public void createProduct(@RequestBody Product product){
        service.createProduct(product);
    }

    @PutMapping ("/{id}")
    public void updateProduct(@PathVariable Long id,
                            @RequestBody Product product){
        service.updateProduct(id, product);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id){
        service.deleteProduct(id);
    }
}
