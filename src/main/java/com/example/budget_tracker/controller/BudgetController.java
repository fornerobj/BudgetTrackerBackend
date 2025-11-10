package com.example.budget_tracker.controller;

import com.example.budget_tracker.domain.Transaction;
import com.example.budget_tracker.domain.User;
import com.example.budget_tracker.dto.BudgetSummaryDto;
import com.example.budget_tracker.exception.UserNotFoundException;
import com.example.budget_tracker.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class BudgetController {

    @Autowired
    private CsvParserFactory csvParserFactory;
    @Autowired
    private BudgetService budgetService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private UserService userService;

    @PostMapping("/upload")
    public ResponseEntity<List<Transaction>> uploadCsv(
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal Jwt jwt) {
        try {
            CsvParserService csvParser = csvParserFactory.getParser(file);
            List<Transaction> transactions = csvParser.parseCsv(file);

            User user = userService.findByAuth0Id(jwt.getSubject());
            if(user == null) {
                throw new UserNotFoundException("User not found with Auth0 ID: " + jwt.getSubject());
            }

            transactions.forEach(tx -> tx.setUser(user));

            List<Transaction> savedTransactions = transactionService.importTransactions(user, transactions);
            return ResponseEntity.ok(savedTransactions);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/summary")
    public ResponseEntity<List<BudgetSummaryDto>> getBudgetSummary(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo,
            @AuthenticationPrincipal Jwt jwt)
    {
        try {
            User user = userService.findByAuth0Id(jwt.getSubject());
            List<BudgetSummaryDto> summaries = budgetService.getBudgetSummaries(
                    user,
                    dateFrom,
                    dateTo
            );
            return ResponseEntity.ok(summaries);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}
