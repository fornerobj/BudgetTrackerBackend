package com.example.budget_tracker.service;

import com.example.budget_tracker.domain.Category;
import com.example.budget_tracker.domain.Transaction;
import com.example.budget_tracker.domain.User;
import com.example.budget_tracker.dto.BudgetSummaryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BudgetService {

    @Autowired
    CategoryService categoryService;
    @Autowired
    TransactionService transactionService;

    public List<BudgetSummaryDto> getBudgetSummaries(User user, LocalDate dateFrom, LocalDate dateTo) {
        List<Category> categories = categoryService.getCategoriesByUser(user);
        return categories.stream().map(cat -> {
            List<Transaction> transactions = transactionService.findAll((root, query, cb) -> cb.and(
                    cb.equal(root.get("category"), cat),
                    cb.equal(root.get("excluded"), false),
                    cb.between(root.get("date"), dateFrom, dateTo)
            ));
            for (Transaction tx : transactions) {
                System.out.println("Transaction: " + tx.getDescription() + ", Amount: " + tx.getAmount());
            }
            double spent = transactions.stream().mapToDouble(Transaction::getAmount).sum();
            double budget = cat.getBudget() != null ? cat.getBudget() : 0.0;
            double remaining = budget - spent;
            return new BudgetSummaryDto(
                    cat.getId(),
                    cat.getName(),
                    cat.getDescription(),
                    budget,
                    spent,
                    remaining
            );
        }).collect(Collectors.toList());
    }
}
