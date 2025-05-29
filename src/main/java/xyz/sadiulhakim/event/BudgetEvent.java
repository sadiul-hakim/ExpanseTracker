package xyz.sadiulhakim.event;

import xyz.sadiulhakim.budget.Budget;

public record BudgetEvent(
        String username,
        double cost,
        Budget budget,
        double percentage
) {
}
