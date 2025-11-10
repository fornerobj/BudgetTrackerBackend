package com.example.budget_tracker.exception;

public class TransactionOwnershipException extends RuntimeException {
    public TransactionOwnershipException(String message) {
        super(message);
    }
}
