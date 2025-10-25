package com.example.budget_tracker.controller;

import com.example.budget_tracker.domain.Transaction;
import com.example.budget_tracker.service.CsvParserFactory;
import com.example.budget_tracker.service.CsvParserService;
import com.example.budget_tracker.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/upload")
public class BudgetController {

    @Autowired
    private CsvParserFactory csvParserFactory;

    @Autowired
    TransactionService transactionService;

    @PostMapping
    public ResponseEntity<List<Transaction>> uploadCsv(@RequestParam("file") MultipartFile file) {
        try {
            CsvParserService csvParser = csvParserFactory.getParser(file);
            List<Transaction> transactions = csvParser.parseCsv(file);
            List<Transaction> savedTransactions = transactionService.importTransactions(transactions);
            return ResponseEntity.ok(savedTransactions);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}
