package com.budgetapp.service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Equivalente dell'array associativo restituito da Transaction::monthlySummary() in PHP.
 */
public class MonthlySummary {

    public record CategoryRow(String category, BigDecimal total) {
    }

    private final String month; // formato "yyyy-MM"
    private final BigDecimal income;
    private final BigDecimal expenses;
    private final BigDecimal balance;
    private final List<CategoryRow> byCategory;

    public MonthlySummary(String month, BigDecimal income, BigDecimal expenses,
                           BigDecimal balance, List<CategoryRow> byCategory) {
        this.month = month;
        this.income = income;
        this.expenses = expenses;
        this.balance = balance;
        this.byCategory = byCategory;
    }

    public String getMonth() {
        return month;
    }

    public BigDecimal getIncome() {
        return income;
    }

    public BigDecimal getExpenses() {
        return expenses;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public List<CategoryRow> getByCategory() {
        return byCategory;
    }
}
