package com.Esercitazione.ProducerConsumer.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.Esercitazione.ProducerConsumer.model.Product;

import java.math.BigDecimal;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("""
            SELECT p
            FROM Product p
            WHERE (:id IS NULL OR p.id = :id)
              AND (:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')))
              AND (:category IS NULL OR LOWER(p.category) LIKE LOWER(CONCAT('%', :category, '%')))
            """)
    List<Product> searchProducts(
            @Param("id") Long id,
            @Param("name") String name,
            @Param("category") String category,
            Sort sort);
}
