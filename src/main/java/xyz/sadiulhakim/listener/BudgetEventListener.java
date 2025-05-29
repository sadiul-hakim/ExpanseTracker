package xyz.sadiulhakim.listener;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import xyz.sadiulhakim.budget.Budget;
import xyz.sadiulhakim.budget.BudgetRepository;
import xyz.sadiulhakim.event.BudgetEvent;

@Component
public class BudgetEventListener {

    private final BudgetRepository repository;

    public BudgetEventListener(BudgetRepository repository) {
        this.repository = repository;
    }

    @Async("defaultTaskExecutor")
    @EventListener
    void budgetExceeded(BudgetEvent event) {
        System.out.printf("Budget %s is %s percent exceeded!", event.budget().getTitle(), event.percentage());

        if (event.percentage() < 100) {
            return;
        }
        Budget budget = event.budget();
        budget.setExceeded(true);
        repository.save(budget);
    }
}
