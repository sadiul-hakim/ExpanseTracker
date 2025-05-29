package xyz.sadiulhakim.budget;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import xyz.sadiulhakim.budget.pojo.BudgetDTO;
import xyz.sadiulhakim.category.Category;
import xyz.sadiulhakim.category.CategoryDTO;
import xyz.sadiulhakim.category.CategoryService;
import xyz.sadiulhakim.exception.EntityNotFoundExecption;
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

    public List<BudgetDTO> findAllByCategory(long categoryId) {

        // Use the current userId
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userService.findByEmail(username);

        Category category = categoryService.findModelById(categoryId);
        List<Budget> budgets = repository.findAllByUserAndCategory(user, category);
        return budgets.stream().map(this::convertToDTO).toList();
    }

    // This method exposes Budget and Budget exposes User.
    // So this method should not be used with controller.
    public List<Budget> findActiveBudgetsForUserAndCategory(Category category, LocalDateTime startDate,
                                                            LocalDateTime endDate) {

        // Use the current userId
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userService.findByEmail(username);
        return repository.findAllByUserAndCategoryAndStartDateLessThanEqualAndEndDateGreaterThanEqual(user, category,
                startDate, endDate);
    }

    public BudgetDTO convertToDTO(Budget budget) {
        return new BudgetDTO(budget.getTitle(), budget.getCategory().getName(), budget.getUser().getId(),
                budget.getAmount(), budget.getStartDate(), budget.getEndDate());
    }

    public boolean delete(long id) {

        try {
            Budget budget = repository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundExecption("Budget is not found with id " + id));
            // Use the current userId
            var username = SecurityContextHolder.getContext().getAuthentication().getName();
            var user = userService.findByEmail(username);
            if (!user.equals(budget.getUser())) {
                throw new UnsupportedActivityException("You are not allowed to delete this budget!");
            }

            repository.delete(budget);
            return true;
        } catch (Exception ex) {
            LOGGER.error("Could not delete budget {}", id);
            return false;
        }
    }

    public boolean deleteAllByCategory(long categoryId) {
        try {
            var username = SecurityContextHolder.getContext().getAuthentication().getName();
            var user = userService.findByEmail(username);
            Category category = categoryService.findModelById(categoryId);
            List<Budget> budgets = repository.findAllByUserAndCategory(user, category);
            repository.deleteAll(budgets);
            return true;
        } catch (Exception ex) {
            LOGGER.error("Could not delete budgets by category {}", categoryId);
            return false;
        }
    }
}
