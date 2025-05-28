package xyz.sadiulhakim.listener;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import xyz.sadiulhakim.event.BudgetExceededEvent;

@Component
public class BudgetEventListener {

    @Async("defaultTaskExecutor")
    @EventListener
    void budgetExceeded(BudgetExceededEvent event) {
        System.out.printf("Budget %s is exceeded!", event.budget().getTitle());
    }
}
