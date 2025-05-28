package xyz.sadiulhakim.budget;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import xyz.sadiulhakim.budget.pojo.BudgetDTO;
import xyz.sadiulhakim.category.Category;
import xyz.sadiulhakim.category.CategoryService;
import xyz.sadiulhakim.exception.UnsupportedActivityException;
import xyz.sadiulhakim.user.UserService;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BudgetService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BudgetService.class);

    public final BudgetRepository repository;
    private final UserService userService;
    private final CategoryService categoryService;

    public BudgetService(BudgetRepository repository, UserService userService, CategoryService categoryService) {
        this.repository = repository;
        this.userService = userService;
        this.categoryService = categoryService;
    }

    public void save(BudgetDTO dto) {

        try {
            if (dto.startDate() == null || dto.endDate() == null || dto.startDate().isAfter(dto.endDate())) {
                throw new UnsupportedActivityException("Start Date can not be after the End Date!");
            }

            // Use the current userId
            var username = SecurityContextHolder.getContext().getAuthentication().getName();
            var user = userService.findByEmail(username);
            Category category = categoryService.findModelByName(dto.category());
            var budget = new Budget();
            budget.setUser(user);
            budget.setCategory(category);
            budget.setTitle(dto.title());
            budget.setAmount(dto.amount());
            budget.setStartDate(dto.startDate());
            budget.setEndDate(dto.endDate());
            repository.save(budget);
        } catch (Exception ex) {
            LOGGER.error("Failed to save budget {}", dto.title());
        }
    }

    public List<Budget> findAll() {

        // Use the current userId
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userService.findByEmail(username);
        return repository.findAllByUser(user);
    }

    public List<Budget> findAllByCategory(Category category) {

        // Use the current userId
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userService.findByEmail(username);
        return repository.findAllByUserAndCategory(user, category);
    }

    public List<Budget> findActiveBudgetsForUserAndCategory(Category category, LocalDateTime startDate,
                                                            LocalDateTime endDate) {

        // Use the current userId
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userService.findByEmail(username);
        return repository.findAllByUserAndCategoryAndStartDateLessThanEqualAndEndDateGreaterThanEqual(user, category,
                startDate, endDate);
    }
}
