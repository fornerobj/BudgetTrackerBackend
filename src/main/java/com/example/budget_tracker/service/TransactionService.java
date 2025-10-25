package com.example.budget_tracker.service;

import com.example.budget_tracker.domain.Transaction;
import com.example.budget_tracker.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;


    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public List<Transaction> getUncategorizedTransactionsNotExcluded() {
           return transactionRepository.findUncategorizedNotExcluded();
    }

    @Transactional
    public Optional<Transaction> updateTransaction(Transaction transaction) {
        return transactionRepository.findById(transaction.getId()).map(existing -> {
            boolean changed = false;
            if (transaction.getCategory() != null && !Objects.equals(existing.getCategory(), transaction.getCategory())) {
                existing.setCategory(transaction.getCategory());
                changed = true;
            }
            if (transaction.getDescription() != null && !Objects.equals(existing.getDescription(), transaction.getDescription())) {
                existing.setDescription(transaction.getDescription());
                changed = true;
            }
            if (transaction.getDate() != null && !Objects.equals(existing.getDate(), transaction.getDate())) {
                existing.setDate(transaction.getDate());
                changed = true;
            }
            if (transaction.getType() != null && !Objects.equals(existing.getType(), transaction.getType())) {
                existing.setType(transaction.getType());
                changed = true;
            }
            if (transaction.getAmount() != null && !Objects.equals(existing.getAmount(), transaction.getAmount())) {
                existing.setAmount(transaction.getAmount());
                changed = true;
            }
            if (existing.isExcluded() != transaction.isExcluded()) {
                existing.setExcluded(transaction.isExcluded());
                changed = true;
            }
            return changed ? transactionRepository.save(existing) : existing;
        });
    }

    @Transactional
    public List<Transaction> bulkUpdateTransactions(List<Transaction> transactions) {
        List<Transaction> updated = new ArrayList<>();
        for (Transaction t : transactions) {
            if (t == null || t.getDate() == null) continue;
            transactionRepository.findById(t.getId()).ifPresent(existing -> {
                boolean changed = false;
                if (t.getCategory() != null && !Objects.equals(existing.getCategory(), t.getCategory())) {
                    existing.setCategory(t.getCategory());
                    changed = true;
                }
                if (t.getDescription() != null && !Objects.equals(existing.getDescription(), t.getDescription())) {
                    existing.setDescription(t.getDescription());
                    changed = true;
                }
                if (t.getDate() != null && !Objects.equals(existing.getDate(), t.getDate())) {
                    existing.setDate(t.getDate());
                    changed = true;
                }
                if (t.getType() != null && !Objects.equals(existing.getType(), t.getType())) {
                    existing.setType(t.getType());
                    changed = true;
                }
                if (t.getAmount() != null && !Objects.equals(existing.getAmount(), t.getAmount())) {
                    existing.setAmount(t.getAmount());
                    changed = true;
                }
                if (existing.isExcluded() != t.isExcluded()) {
                    existing.setExcluded(t.isExcluded());
                    changed = true;
                }
                if (changed) updated.add(transactionRepository.save(existing));
            });
        }
        return updated;
    }

    @Transactional
    public List<Transaction> importTransactions(List<Transaction> transactions) {
        if (transactions == null || transactions.isEmpty()) {
            return Collections.emptyList();
        }

        List<Transaction> toSave = transactions.stream()
                .filter(Objects::nonNull)
                .filter(tx -> !transactionRepository.existsByDateAndDescriptionAndAmount(
                        tx.getDate(), tx.getDescription(), tx.getAmount()))
                .toList();

        return transactionRepository.saveAll(toSave);
    }

    private record TxKey(LocalDate date, String description, Double amount) {}
}
