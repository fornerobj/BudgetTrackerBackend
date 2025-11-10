package com.example.budget_tracker.service;

import com.example.budget_tracker.domain.Transaction;
import com.example.budget_tracker.domain.User;
import com.example.budget_tracker.dto.TransactionUpdateDto;
import com.example.budget_tracker.exception.TransactionNotFoundException;
import com.example.budget_tracker.exception.TransactionOwnershipException;
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
    public Optional<Transaction> updateTransaction(User user, Long id, TransactionUpdateDto update) {
        // Ensure the transaction belongs to the user
        Optional <Transaction> existingTransaction = transactionRepository.findById(id);
        if(existingTransaction.isEmpty()) {
            throw new TransactionNotFoundException("Transaction not found with ID: " + id);
        }
        if(!existingTransaction.get().getUser().getId().equals(user.getId())) {
            throw new TransactionOwnershipException("User does not own the transaction with ID: " + id);
        }
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
    public List<Transaction> importTransactions(User user, List<Transaction> transactions) {
        if (transactions == null || transactions.isEmpty()) {
            return Collections.emptyList();
        }

        List<Transaction> toSave = transactions.stream()
                .filter(Objects::nonNull)
                .filter(tx -> !transactionRepository.existsByDateAndDescriptionAndAmountAndUser(
                        tx.getDate(), tx.getDescription(), tx.getAmount(), user))
                .toList();

        return transactionRepository.saveAll(toSave);
    }

    public Optional<Transaction> deleteTransaction(User user, Long id) {
        Optional <Transaction> existingTransaction = transactionRepository.findById(id);
        if(existingTransaction.isEmpty()) {
            throw new TransactionNotFoundException("Transaction not found with ID: " + id);
        }
        if(!existingTransaction.get().getUser().getId().equals(user.getId())) {
            throw new TransactionOwnershipException("User does not own the transaction with ID: " + id);
        }

        return transactionRepository.findById(id)
                .map(transaction -> {
                    transactionRepository.delete(transaction);
                    return transaction;
                });
    }
}
