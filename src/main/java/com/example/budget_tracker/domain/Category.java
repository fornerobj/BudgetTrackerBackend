package com.example.budget_tracker.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    @Column(length = 1000)
    private String description;
    private Double budget;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Category (String name, String description, Double budget) {
        this.name = name;
        this.description = description;
        this.budget = budget;
    }

}
