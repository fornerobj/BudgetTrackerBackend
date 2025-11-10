package com.example.budget_tracker.repository;

import com.example.budget_tracker.domain.Category;
import com.example.budget_tracker.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByUser(User user);

}
