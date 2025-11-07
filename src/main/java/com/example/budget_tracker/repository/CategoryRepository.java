package com.example.budget_tracker.repository;

import com.example.budget_tracker.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
