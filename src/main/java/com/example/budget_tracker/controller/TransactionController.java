package com.example.budget_tracker.controller;

import com.example.budget_tracker.domain.Transaction;
import com.example.budget_tracker.dto.transactionDto;
import com.example.budget_tracker.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @GetMapping("/uncategorized")
    public ResponseEntity<List<Transaction>> getUncategorizedNotExcluded() {
       return ResponseEntity.ok(transactionService.getUncategorizedTransactionsNotExcluded());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Transaction> updateTransaction(
            @RequestBody Transaction transaction) {
        return transactionService.updateTransaction(transaction)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/bulk")
    public ResponseEntity<List<Transaction>> bulkUpdateTransactions(
            @RequestBody List<Transaction> transactions) {
        List<Transaction> updated = transactionService.bulkUpdateTransactions(transactions);
        return ResponseEntity.ok(updated);
    }
}
