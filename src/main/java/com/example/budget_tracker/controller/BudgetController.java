package com.example.budget_tracker.controller;

import com.example.budget_tracker.domain.Transaction;
import com.example.budget_tracker.dto.BudgetSummaryDto;
import com.example.budget_tracker.service.BudgetService;
import com.example.budget_tracker.service.CsvParserFactory;
import com.example.budget_tracker.service.CsvParserService;
import com.example.budget_tracker.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
public class BudgetController {

    @Autowired
    private CsvParserFactory csvParserFactory;
    @Autowired
    private BudgetService budgetService;
    @Autowired
    TransactionService transactionService;

    @PostMapping("/upload")
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

    @GetMapping("/summary")
    public ResponseEntity<List<BudgetSummaryDto>> getBudgetSummary(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo)
    {
        try {
            List<BudgetSummaryDto> summaries = budgetService.getBudgetSummaries(
                    dateFrom,
                    dateTo
            );
            return ResponseEntity.ok(summaries);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}
