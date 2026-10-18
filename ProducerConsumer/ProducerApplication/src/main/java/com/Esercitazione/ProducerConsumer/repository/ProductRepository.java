package com.Esercitazione.ProducerConsumer.repository;

import org.springframework.data.domain.Pageable;
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
                          AND p.name ILIKE CONCAT('%', COALESCE(:name, ''), '%')
                          AND p.category ILIKE CONCAT('%', COALESCE(:category, ''), '%')
                        """)
        List<Product> searchProducts(
                        @Param("id") Long id,
                        @Param("name") String name,
                        @Param("category") String category,
                        Pageable pageable);
}
