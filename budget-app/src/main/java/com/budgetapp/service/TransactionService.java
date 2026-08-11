package com.budgetapp.service;

import com.budgetapp.model.Category;
import com.budgetapp.model.Transaction;
import com.budgetapp.repository.TransactionRepository;
import com.budgetapp.repository.TransactionRepository.CategoryTotal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class TransactionService {

    private static final Set<String> ALLOWED_MIME_TYPES = Set.of(
            "image/jpeg", "image/png", "application/pdf"
    );

    private final TransactionRepository transactionRepository;
    private final Path uploadDir;

    public TransactionService(TransactionRepository transactionRepository,
                               @Value("${app.upload.dir}") String uploadDirProperty) throws IOException {
        this.transactionRepository = transactionRepository;
        this.uploadDir = Path.of(uploadDirProperty).toAbsolutePath().normalize();
        Files.createDirectories(this.uploadDir);
    }

    public List<Transaction> findAllForUser(Long userId) {
        return transactionRepository.findByUserIdOrderByDateDesc(userId);
    }

    public Optional<Transaction> find(Long id, Long userId) {
        return transactionRepository.findByIdAndUserId(id, userId);
    }

    public Transaction create(Long userId, String description, LocalDate date, BigDecimal amount,
                               Category category, MultipartFile receiptFile) {
        String receiptPath = handleUpload(receiptFile);
        Transaction t = new Transaction(userId, description, date, amount, category, receiptPath);
        return transactionRepository.save(t);
    }

    public Transaction update(Long id, Long userId, String description, LocalDate date, BigDecimal amount,
                               Category category, MultipartFile receiptFile) {
        Transaction t = transactionRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new IllegalArgumentException("Transazione non trovata"));

        t.setDescription(description);
        t.setDate(date);
        t.setAmount(amount);
        t.setCategory(category);

        String newReceiptPath = handleUpload(receiptFile);
        if (newReceiptPath != null) {
            t.setReceipt(newReceiptPath);
        }

        return transactionRepository.save(t);
    }

    public void delete(Long id, Long userId) {
        transactionRepository.deleteByIdAndUserId(id, userId);
    }

    /**
     * Equivalente di Transaction::monthlySummary(). Se month è null usa il mese corrente.
     */
    public MonthlySummary monthlySummary(Long userId, String month) {
        String resolvedMonth = (month != null && !month.isBlank())
                ? month
                : YearMonth.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));

        YearMonth ym = YearMonth.parse(resolvedMonth);
        LocalDate start = ym.atDay(1);
        LocalDate end = ym.plusMonths(1).atDay(1);

        List<CategoryTotal> rawByCategory = transactionRepository.sumByCategoryForMonth(userId, start, end);

        BigDecimal income = BigDecimal.ZERO;
        BigDecimal expenses = BigDecimal.ZERO;
        List<MonthlySummary.CategoryRow> byCategory = new java.util.ArrayList<>();
        for (CategoryTotal row : rawByCategory) {
            Category category = Category.valueOf(row.getCategory());
            byCategory.add(new MonthlySummary.CategoryRow(category.getLabel(), row.getTotal()));

            if (category == Category.STIPENDIO) {
                income = income.add(row.getTotal());
            } else {
                expenses = expenses.add(row.getTotal());
            }
        }

        return new MonthlySummary(resolvedMonth, income, expenses, income.subtract(expenses), byCategory);
    }

    /**
     * Salva il file di ricevuta caricato (equivalente di TransactionController::handleUpload() in PHP).
     * Ritorna il percorso relativo del file salvato, oppure null se non è stato caricato/valido nulla.
     */
    private String handleUpload(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_MIME_TYPES.contains(contentType)) {
            return null;
        }

        String originalFilename = StringUtils.cleanPath(
                file.getOriginalFilename() != null ? file.getOriginalFilename() : ""
        );
        String extension = "";
        int dotIndex = originalFilename.lastIndexOf('.');
        if (dotIndex >= 0) {
            extension = originalFilename.substring(dotIndex + 1);
        }

        String filename = "receipt_" + UUID.randomUUID() + (extension.isBlank() ? "" : "." + extension);
        Path destination = uploadDir.resolve(filename);

        try {
            Files.copy(file.getInputStream(), destination);
        } catch (IOException e) {
            return null;
        }

        return "uploads/" + filename;
    }
}
