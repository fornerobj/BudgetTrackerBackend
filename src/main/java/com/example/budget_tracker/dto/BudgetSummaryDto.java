package com.example.budget_tracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BudgetSummaryDto {
    private Long categoryId;
    private String name;
    private String description;
    private Double budget;
    private Double spent;
    private Double remaining;
}
