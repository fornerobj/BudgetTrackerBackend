package com.example.budget_tracker.spec;

import com.example.budget_tracker.domain.Transaction;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;

public class TransactionSpecification {
    public static Specification<Transaction> hasCategoryIn(List<String> categories) {
        return (root, query, criteriaBuilder) ->
                (categories == null || categories.isEmpty())
                        ? null
                        : root.get("category").in(categories);
    }

    public static Specification<Transaction> hasType(List<String> types) {
        return (root, query, criteriaBuilder) ->
                (types == null || types.isEmpty())
                        ? null
                        : root.get("type").in(types);
    }

    public static Specification<Transaction> isExcluded(Boolean excluded) {
        return (root, query, criteriaBuilder) ->
                excluded == null
                        ? null
                        : criteriaBuilder.equal(root.get("excluded"), excluded);
    }

    public static Specification<Transaction> dateBetween(LocalDate from, LocalDate to) {
        return (root, query, cb) -> {
            if (from != null && to != null) {
                return cb.between(root.get("date"), from, to);
            } else if (from != null) {
                return cb.greaterThanOrEqualTo(root.get("date"), from);
            } else if (to != null) {
                return cb.lessThanOrEqualTo(root.get("date"), to);
            } else {
                return null;
            }
        };
    }

    public static Specification<Transaction> amountBetween(Double min, Double max) {
        return (root, query, cb) -> {
            if (min != null && max != null) {
                return cb.between(root.get("amount"), min, max);
            } else if (min != null) {
                return cb.greaterThanOrEqualTo(root.get("amount"), min);
            } else if (max != null) {
                return cb.lessThanOrEqualTo(root.get("amount"), max);
            } else {
                return null;
            }
        };
    }
}
