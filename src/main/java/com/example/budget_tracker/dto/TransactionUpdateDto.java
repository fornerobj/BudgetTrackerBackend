package com.example.budget_tracker.dto;

import com.example.budget_tracker.domain.Category;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Optional;

@Getter
@Setter
public class TransactionUpdateDto {
    private Optional<LocalDate> date = Optional.empty();
    private Optional<String> description = Optional.empty();
    private Optional<Long> categoryId = Optional.empty();
    private Optional<String> type = Optional.empty();
    private Optional<Double> amount = Optional.empty();
    private Optional<Boolean> excluded = Optional.empty();
}
