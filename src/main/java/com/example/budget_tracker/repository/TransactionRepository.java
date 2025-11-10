package com.example.budget_tracker.repository;

import com.example.budget_tracker.domain.Transaction;
import com.example.budget_tracker.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long>, JpaSpecificationExecutor<Transaction> {
    Optional<Transaction> findByUser(User user);
    boolean existsByDateAndDescriptionAndAmountAndUser(LocalDate date, String description, Double amount, User user);
}
