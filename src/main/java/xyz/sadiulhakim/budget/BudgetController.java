package xyz.sadiulhakim.budget;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.sadiulhakim.budget.pojo.BudgetDTO;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/budget")
public class BudgetController {

    private static final String MESSAGE = "message";

    private final BudgetService budgetService;

    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    @RateLimiter(name = "defaultRateLimiter")
    @PostMapping
    ResponseEntity<?> save(@RequestBody @Valid BudgetDTO dto) {
        budgetService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of(MESSAGE, "Successfully created budget " + dto.title()));
    }

    @RateLimiter(name = "defaultRateLimiter")
    @GetMapping("/category/{categoryId}")
    ResponseEntity<List<BudgetDTO>> getAllBudgets(@PathVariable long categoryId) {
        return ResponseEntity.ok(budgetService.findAllByCategory(categoryId));
    }

    @RateLimiter(name = "defaultRateLimiter")
    @DeleteMapping("/{id}")
    ResponseEntity<?> delete(@PathVariable long id) {
        boolean deleted = budgetService.delete(id);
        return deleted ?
                ResponseEntity.ok(Map.of(MESSAGE, "Successfully deleted budget")) :
                ResponseEntity.ok(Map.of(MESSAGE, "Failed to delete budget!"));
    }

    @RateLimiter(name = "defaultRateLimiter")
    @DeleteMapping("/category/{categoryId}")
    ResponseEntity<?> deleteAllByCategory(@PathVariable long categoryId) {
        boolean deleted = budgetService.deleteAllByCategory(categoryId);
        return deleted ?
                ResponseEntity.ok(Map.of(MESSAGE, "Successfully deleted budgets")) :
                ResponseEntity.ok(Map.of(MESSAGE, "Failed to delete budgets!"));
    }
}
