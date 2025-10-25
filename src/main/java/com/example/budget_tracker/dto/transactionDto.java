package com.example.budget_tracker.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class transactionDto {
    private LocalDate date;
    private String description;
    private String category;
    private String type;
    private Double amount;
    private boolean excluded;

    public transactionDto(LocalDate date, String description, String category, String type, Double amount) {
        this.date = date;
        this.description = description;
        this.category = category;
        this.type = type;
        this.amount = amount;
        this.excluded = false;
    }

    @Override
    public String toString() {
        return "transactionDto{" +
                "date=" + date +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", type='" + type + '\'' +
                ", amount=" + amount +
                ", excluded=" + excluded +
                '}';
    }
}
