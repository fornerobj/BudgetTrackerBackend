package com.example.budget_tracker.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;

    @Column(length = 1000)
    private String description;
    private String category;
    private String type;
    private Double amount;
    private boolean excluded;


    public Transaction(LocalDate date, String description, String category, String type, Double amount) {
        this.date = date;
        this.description = description;
        this.category = category;
        this.type = type;
        this.amount = amount;
        this.excluded = false;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", date=" + date +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", type='" + type + '\'' +
                ", amount=" + amount +
                ", excluded=" + excluded +
                '}';
    }
}
