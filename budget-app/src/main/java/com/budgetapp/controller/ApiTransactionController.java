package com.budgetapp.controller;

import com.budgetapp.model.Category;
import com.budgetapp.model.Transaction;
import com.budgetapp.service.TransactionService;
import com.budgetapp.util.SessionUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * REST API per le transazioni. Equivalente di ApiController.php.
 *
 * Endpoint:
 *   GET    /api/transactions        -> elenco transazioni dell'utente loggato
 *   GET    /api/transactions/{id}   -> singola transazione
 *   POST   /api/transactions        -> crea una transazione (JSON nel body)
 *   PUT    /api/transactions/{id}   -> modifica una transazione (JSON nel body)
 *   DELETE /api/transactions/{id}   -> cancella una transazione
 *
 * Autenticazione: richiede una sessione attiva (login effettuato via form web),
 * esattamente come nel progetto PHP originale.
 */
@RestController
@RequestMapping("/api/transactions")
public class ApiTransactionController {

    private final TransactionService transactionService;

    public ApiTransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    public record TransactionPayload(String description, String date, BigDecimal amount, String category) {
    }

    @GetMapping
    public ResponseEntity<?> all(HttpServletRequest request) {
        Long userId = requireAuth(request);
        if (userId == null) {
            return unauthorized();
        }
        return ResponseEntity.ok(transactionService.findAllForUser(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> one(@PathVariable Long id, HttpServletRequest request) {
        Long userId = requireAuth(request);
        if (userId == null) {
            return unauthorized();
        }
        Optional<Transaction> transaction = transactionService.find(id, userId);
        return transaction.<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> notFound());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody TransactionPayload payload, HttpServletRequest request) {
        Long userId = requireAuth(request);
        if (userId == null) {
            return unauthorized();
        }

        String error = validate(payload);
        if (error != null) {
            return ResponseEntity.unprocessableEntity().body(Map.of("error", error));
        }

        Transaction created = transactionService.create(
                userId, payload.description(), LocalDate.parse(payload.date()),
                payload.amount(), Category.fromLabel(payload.category()), null
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody TransactionPayload payload,
                                     HttpServletRequest request) {
        Long userId = requireAuth(request);
        if (userId == null) {
            return unauthorized();
        }
        if (transactionService.find(id, userId).isEmpty()) {
            return notFound();
        }

        String error = validate(payload);
        if (error != null) {
            return ResponseEntity.unprocessableEntity().body(Map.of("error", error));
        }

        Transaction updated = transactionService.update(
                id, userId, payload.description(), LocalDate.parse(payload.date()),
                payload.amount(), Category.fromLabel(payload.category()), null
        );
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, HttpServletRequest request) {
        Long userId = requireAuth(request);
        if (userId == null) {
            return unauthorized();
        }
        if (transactionService.find(id, userId).isEmpty()) {
            return notFound();
        }
        transactionService.delete(id, userId);
        return ResponseEntity.ok(Map.of("message", "Transazione eliminata."));
    }

    private String validate(TransactionPayload p) {
        if (p.description() == null || p.description().isBlank()
                || p.date() == null || p.date().isBlank()
                || p.amount() == null
                || p.category() == null || p.category().isBlank()) {
            return "Campi obbligatori mancanti: description, date, amount, category.";
        }
        if (!Category.isValid(p.category())) {
            return "Categoria non valida.";
        }
        return null;
    }

    private Long requireAuth(HttpServletRequest request) {
        return SessionUtil.isLoggedIn(request) ? SessionUtil.userId(request) : null;
    }

    private ResponseEntity<?> unauthorized() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Non autenticato. Effettua il login prima di usare la API."));
    }

    private ResponseEntity<?> notFound() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Transazione non trovata."));
    }
}
