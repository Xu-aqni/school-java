package com.budgetapp.controller;

import com.budgetapp.model.Category;
import com.budgetapp.model.Transaction;
import com.budgetapp.service.MonthlySummary;
import com.budgetapp.service.TransactionService;
import com.budgetapp.util.SessionUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpServletRequest request) {
        Long userId = SessionUtil.userId(request);
        MonthlySummary summary = transactionService.monthlySummary(userId, null);
        List<Transaction> transactions = transactionService.findAllForUser(userId);

        model.addAttribute("summary", summary);
        model.addAttribute("transactions", transactions);
        return "transaction/dashboard";
    }

    @GetMapping("/transactions")
    public String index(Model model, HttpServletRequest request) {
        model.addAttribute("transactions", transactionService.findAllForUser(SessionUtil.userId(request)));
        return "transaction/index";
    }

    @GetMapping("/transactions/create")
    public String showCreate(Model model) {
        model.addAttribute("categories", Category.values());
        return "transaction/create";
    }

    @PostMapping("/transactions")
    public String create(@RequestParam String description,
                          @RequestParam String date,
                          @RequestParam String amount,
                          @RequestParam String category,
                          @RequestParam(required = false) MultipartFile receipt,
                          Model model,
                          HttpServletRequest request) {

        ValidationResult validation = validate(description, date, amount, category);
        if (!validation.valid()) {
            model.addAttribute("error", validation.error());
            model.addAttribute("categories", Category.values());
            return "transaction/create";
        }

        transactionService.create(
                SessionUtil.userId(request),
                validation.description(),
                validation.date(),
                validation.amount(),
                validation.category(),
                receipt
        );

        return "redirect:/transactions";
    }

    @GetMapping("/transactions/{id}/edit")
    public String showEdit(@PathVariable Long id, Model model, HttpServletRequest request) {
        Optional<Transaction> transaction = transactionService.find(id, SessionUtil.userId(request));

        if (transaction.isEmpty()) {
            model.addAttribute("error", "Transazione non trovata.");
            return "error/404";
        }

        model.addAttribute("transaction", transaction.get());
        model.addAttribute("categories", Category.values());
        return "transaction/edit";
    }

    @PostMapping("/transactions/{id}")
    public String update(@PathVariable Long id,
                          @RequestParam String description,
                          @RequestParam String date,
                          @RequestParam String amount,
                          @RequestParam String category,
                          @RequestParam(required = false) MultipartFile receipt,
                          Model model,
                          HttpServletRequest request) {

        Long userId = SessionUtil.userId(request);
        ValidationResult validation = validate(description, date, amount, category);

        if (!validation.valid()) {
            Optional<Transaction> transaction = transactionService.find(id, userId);
            model.addAttribute("transaction", transaction.orElse(null));
            model.addAttribute("categories", Category.values());
            model.addAttribute("error", validation.error());
            return "transaction/edit";
        }

        transactionService.update(
                id, userId,
                validation.description(),
                validation.date(),
                validation.amount(),
                validation.category(),
                receipt
        );

        return "redirect:/transactions";
    }

    @PostMapping("/transactions/{id}/delete")
    public String delete(@PathVariable Long id, HttpServletRequest request) {
        transactionService.delete(id, SessionUtil.userId(request));
        return "redirect:/transactions";
    }

    /**
     * Equivalente di TransactionController::readAndValidateInput() in PHP.
     */
    private ValidationResult validate(String description, String date, String amountRaw, String categoryRaw) {
        String desc = description == null ? "" : description.trim();
        String d = date == null ? "" : date.trim();
        String cat = categoryRaw == null ? "" : categoryRaw.trim();

        BigDecimal amount;
        try {
            amount = new BigDecimal(amountRaw);
        } catch (Exception e) {
            amount = BigDecimal.ZERO;
        }

        LocalDate parsedDate;
        try {
            parsedDate = LocalDate.parse(d);
        } catch (Exception e) {
            parsedDate = null;
        }

        if (desc.isEmpty() || parsedDate == null || amount.compareTo(BigDecimal.ZERO) <= 0
                || !Category.isValid(cat)) {
            return new ValidationResult(false, "Compila tutti i campi correttamente.",
                    desc, parsedDate, amount, null);
        }

        return new ValidationResult(true, null, desc, parsedDate, amount, Category.fromLabel(cat));
    }

    private record ValidationResult(boolean valid, String error, String description,
                                     LocalDate date, BigDecimal amount, Category category) {
    }
}
