package com.example.budget_tracker.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryCreateDto {
    private String name;
    private String description;
    private Double budget;
}
