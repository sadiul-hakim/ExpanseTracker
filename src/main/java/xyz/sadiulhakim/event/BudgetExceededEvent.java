package xyz.sadiulhakim.event;

import xyz.sadiulhakim.budget.Budget;

public record BudgetExceededEvent(
        String username,
        double cost,
        Budget budget
) {
}
