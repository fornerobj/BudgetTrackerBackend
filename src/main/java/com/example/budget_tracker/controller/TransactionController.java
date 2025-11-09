package com.example.budget_tracker.controller;

import com.example.budget_tracker.domain.Transaction;
import com.example.budget_tracker.dto.TransactionUpdateDto;
import com.example.budget_tracker.service.TransactionService;
import com.example.budget_tracker.spec.TransactionSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @GetMapping
    public ResponseEntity<List<Transaction>> getAllWithSpecification(
            @RequestParam(required = false) List<String> categories,
            @RequestParam(required = false) List<String> type,
            @RequestParam(required = false) Boolean excluded,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo,
            @RequestParam(required = false) Double amountMin,
            @RequestParam(required = false) Double amountMax) {

        Specification<Transaction> spec = Specification.<Transaction>unrestricted()
                .and(TransactionSpecification.hasCategoryIn(categories))
                .and(TransactionSpecification.hasType(type))
                .and(TransactionSpecification.isExcluded(excluded))
                .and(TransactionSpecification.dateBetween(dateFrom, dateTo))
                .and(TransactionSpecification.amountBetween(amountMin, amountMax));

        List<Transaction> result = transactionService.findAll(spec);
        System.out.println(result);
        return ResponseEntity.ok(result);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Transaction> updateTransaction(
            @PathVariable Long id,
            @RequestBody TransactionUpdateDto update) {
        return transactionService.updateTransaction(id, update)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Transaction> deleteTransaction(@PathVariable Long id) {
        return transactionService.deleteTransaction(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
