package com.example.budget_tracker.service;

import com.example.budget_tracker.domain.Category;
import com.example.budget_tracker.dto.CategoryCreateDto;
import com.example.budget_tracker.dto.CategoryUpdateDto;
import com.example.budget_tracker.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    //Create
    public Optional<Category> createCategory(CategoryCreateDto category) {
        Category newCategory = new Category(category.getName(), category.getDescription(), category.getBudget());
        return Optional.of(categoryRepository.save(newCategory));
    }

    //Read
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    //Update
    public Optional<Category> updateCategory(Long id, CategoryUpdateDto update) {
        return categoryRepository.findById(id)
                .map(category -> {
                    update.getName().ifPresent(category::setName);
                    update.getDescription().ifPresent(category::setDescription);
                    update.getBudget().ifPresent(category::setBudget);
                    return categoryRepository.save(category);
                });
    }

    //Delete
    public Optional<Category> deleteCategory(Long id) {
        return categoryRepository.findById(id)
                .map(category -> {
                    categoryRepository.delete(category);
                    return category;
                });
    }

}
