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
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    private String type;
    private Double amount;
    private boolean excluded;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    public Transaction(LocalDate date, String description, Category category, String type, Double amount) {
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
