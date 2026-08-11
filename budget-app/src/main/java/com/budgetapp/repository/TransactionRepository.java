package com.budgetapp.repository;

import com.budgetapp.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByUserIdOrderByDateDesc(Long userId);

    Optional<Transaction> findByIdAndUserId(Long id, Long userId);

    void deleteByIdAndUserId(Long id, Long userId);

    // Equivalente della query GROUP BY category ... DATE_FORMAT(date, '%Y-%m') = :month del progetto PHP
    @Query("SELECT t.category AS category, SUM(t.amount) AS total " +
           "FROM Transaction t " +
           "WHERE t.userId = :userId AND t.date >= :start AND t.date < :end " +
           "GROUP BY t.category")
    List<CategoryTotal> sumByCategoryForMonth(@Param("userId") Long userId,
                                               @Param("start") LocalDate start,
                                               @Param("end") LocalDate end);

    interface CategoryTotal {
        String getCategory();
        java.math.BigDecimal getTotal();
    }
}
