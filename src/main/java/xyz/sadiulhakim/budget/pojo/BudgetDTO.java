package xyz.sadiulhakim.budget.pojo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record BudgetDTO(

        @NotBlank
        @Size(max = 120)
        String title,

        @NotBlank
        String category,
        long user,

        @PositiveOrZero
        double amount,
        LocalDateTime startDate,
        LocalDateTime endDate
) {
}
