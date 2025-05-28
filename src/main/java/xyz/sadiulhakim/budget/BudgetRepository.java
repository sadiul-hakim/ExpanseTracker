package xyz.sadiulhakim.budget;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.sadiulhakim.category.Category;
import xyz.sadiulhakim.user.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BudgetRepository extends JpaRepository<Budget, Long> {

    Optional<Budget> findByTitle(String title);

    List<Budget> findAllByUser(User user);

    List<Budget> findAllByUserAndCategory(User user, Category category);

    List<Budget> findAllByUserAndCategoryAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            User user, Category category, LocalDateTime startDate, LocalDateTime endDate
    );
}
