package com.example.budget_tracker.controller;

import com.example.budget_tracker.domain.Transaction;
import com.example.budget_tracker.domain.User;
import com.example.budget_tracker.dto.TransactionUpdateDto;
import com.example.budget_tracker.exception.UserNotFoundException;
import com.example.budget_tracker.service.TransactionService;
import com.example.budget_tracker.service.UserService;
import com.example.budget_tracker.spec.TransactionSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<Transaction>> getAllWithSpecification(
            @RequestParam(required = false) List<String> categories,
            @RequestParam(required = false) List<String> type,
            @RequestParam(required = false) Boolean excluded,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo,
            @RequestParam(required = false) Double amountMin,
            @RequestParam(required = false) Double amountMax,
            @AuthenticationPrincipal Jwt jwt
    ) {

        User user = userService.findByAuth0Id(jwt.getSubject());
        if(user == null) {
            throw new UserNotFoundException("User not found with Auth0 ID: " + jwt.getSubject());
        }

        Specification<Transaction> spec = Specification.<Transaction>unrestricted()
                .and(TransactionSpecification.hasCategoryIn(categories))
                .and(TransactionSpecification.hasType(type))
                .and(TransactionSpecification.belongsToUser(user))
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
            @RequestBody TransactionUpdateDto update,
            @AuthenticationPrincipal Jwt jwt
    ) {
        User user = userService.findByAuth0Id(jwt.getSubject());
        if(user == null) {
            throw new UserNotFoundException("User not found with Auth0 ID: " + jwt.getSubject());
        }

        return transactionService.updateTransaction(user, id, update)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Transaction> deleteTransaction(
            @PathVariable Long id,
            @AuthenticationPrincipal Jwt jwt
    ) {
        User user = userService.findByAuth0Id(jwt.getSubject());
        if(user == null) {
            throw new UserNotFoundException("User not found with Auth0 ID: " + jwt.getSubject());
        }

        return transactionService.deleteTransaction(user, id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
