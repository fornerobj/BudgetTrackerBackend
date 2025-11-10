package com.example.budget_tracker.exception;

import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Void> handleUserNotFound() {
        return ResponseEntity.status(401).build();
    }

    @ExceptionHandler(CategoryOwnershipException.class)
    public ResponseEntity<Void> handleCategoryOwnership() {
        return ResponseEntity.status(403).build();
    }

    @ExceptionHandler(TransactionOwnershipException.class)
    public ResponseEntity<Void> handleTransactionOwnership() {
        return ResponseEntity.status(403).build();
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<Void> handleCategoryNotFound() {
        return ResponseEntity.status(404).build();
    }

    @ExceptionHandler(TransactionNotFoundException.class)
    public ResponseEntity<Void> handleTransactionNotFound() {
        return ResponseEntity.status(404).build();
    }
}
