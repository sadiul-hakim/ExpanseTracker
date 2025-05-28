package xyz.sadiulhakim.budget.pojo;

import java.time.LocalDateTime;

public record BudgetDTO(
        String title,
        String category,
        double amount,
        LocalDateTime startDate,
        LocalDateTime endDate
) {
}
