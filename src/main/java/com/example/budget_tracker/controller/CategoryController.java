package com.example.budget_tracker.controller;

import com.example.budget_tracker.domain.Category;
import com.example.budget_tracker.domain.User;
import com.example.budget_tracker.dto.CategoryCreateDto;
import com.example.budget_tracker.dto.CategoryUpdateDto;
import com.example.budget_tracker.exception.CategoryOwnershipException;
import com.example.budget_tracker.service.CategoryService;
import com.example.budget_tracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<Category> createCategory(
            @RequestBody CategoryCreateDto category,
            @AuthenticationPrincipal Jwt jwt
            ) {
        User user = userService.findByAuth0Id(jwt.getSubject());
        if(user == null) {
            return ResponseEntity.status(401).build();
        }
        Optional<Category> createdCategory = categoryService.createCategory(user, category);
        return createdCategory
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(500).build());
    }

    @GetMapping
    public ResponseEntity<List<Category>> getUserCategories(
            @AuthenticationPrincipal Jwt jwt
    ) {
        User user = userService.findByAuth0Id(jwt.getSubject());
        if(user == null) {
            return ResponseEntity.status(401).build();
        }
        List<Category> categories = categoryService.getCategoriesByUser(user);
        if(categories.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(categories);
    }


    @PatchMapping("/{id}")
    public ResponseEntity<Category> updateCategory(
            @PathVariable Long id,
            @RequestBody CategoryUpdateDto update,
            @AuthenticationPrincipal Jwt jwt
    ) {
        User user = userService.findByAuth0Id(jwt.getSubject());
        if(user == null) {
            return ResponseEntity.status(401).build();
        }

        return categoryService.updateCategory(user, id, update)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Category> deleteCategory(
            @PathVariable Long id,
            @AuthenticationPrincipal Jwt jwt
    ) {
        User user = userService.findByAuth0Id(jwt.getSubject());
        if(user == null) {
            return ResponseEntity.status(401).build();
        }

        return categoryService.deleteCategory(user, id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());

    }
}
