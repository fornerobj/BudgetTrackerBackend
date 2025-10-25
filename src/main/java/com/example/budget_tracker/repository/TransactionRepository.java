package com.example.budget_tracker.repository;

import com.example.budget_tracker.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    boolean existsByDateAndDescriptionAndAmount(LocalDate date, String description, Double amount);

   @Query("select t from Transaction t where t.category is null or trim(t.category) = '' and t.excluded = false")
   List<Transaction> findUncategorizedNotExcluded();
}
