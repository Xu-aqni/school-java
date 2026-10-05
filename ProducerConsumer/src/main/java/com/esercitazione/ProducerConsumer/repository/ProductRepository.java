package com.Esercitazione.ProducerConsumer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Esercitazione.ProducerConsumer.model.Product;
import java.util.List;


public interface ProductRepository extends JpaRepository<Product, Long> {
    
    List<Product> findByCategory(String category);

    List<Product> findByName(String name);

    List<Product> findAllByOrderByPriceDesc();
}
