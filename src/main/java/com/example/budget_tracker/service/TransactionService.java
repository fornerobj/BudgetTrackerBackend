package com.example.budget_tracker.service;

import com.example.budget_tracker.domain.Transaction;
import com.example.budget_tracker.dto.TransactionUpdateDto;
import com.example.budget_tracker.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private CategoryService categoryService;


    public List<Transaction> findAll(Specification<Transaction> spec) {
        return transactionRepository.findAll(spec);
    }

    @Transactional
    public Optional<Transaction> updateTransaction(Long id, TransactionUpdateDto update) {
        return transactionRepository.findById(id)
                .map(transaction -> {
                    update.getDate().ifPresent(transaction::setDate);
                    update.getDescription().ifPresent(transaction::setDescription);
                    update.getCategoryId().ifPresent(categoryId -> {
                        if (categoryId == -1L) {
                            transaction.setCategory(null);
                        } else {
                            transaction.setCategory(categoryService.getCategoryById(categoryId).orElse(null));
                        }
                    });
                    update.getType().ifPresent(transaction::setType);
                    update.getAmount().ifPresent(transaction::setAmount);
                    update.getExcluded().ifPresent(transaction::setExcluded);
                    return transactionRepository.save(transaction);
                });
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

    public Optional<Transaction> deleteTransaction(Long id) {
        return transactionRepository.findById(id)
                .map(transaction -> {
                    transactionRepository.delete(transaction);
                    return transaction;
                });
    }
}
