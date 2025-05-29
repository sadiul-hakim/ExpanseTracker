package xyz.sadiulhakim.budget;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.Check;
import xyz.sadiulhakim.category.Category;
import xyz.sadiulhakim.user.User;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Check(constraints = "amount > 0")
public class Budget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 120)
    @Column(length = 120, nullable = false, unique = true)
    private String title;

    @ManyToOne
    private User user;

    @ManyToOne
    private Category category;

    private double amount;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private boolean exceeded;

    public Budget() {
    }

    public Budget(Long id, String title, User user, Category category, double amount, LocalDateTime startDate,
                  LocalDateTime endDate, boolean exceeded) {
        this.id = id;
        this.title = title;
        this.user = user;
        this.category = category;
        this.amount = amount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.exceeded = exceeded;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public boolean isExceeded() {
        return exceeded;
    }

    public void setExceeded(boolean exceeded) {
        this.exceeded = exceeded;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Budget budget = (Budget) o;
        return Double.compare(amount, budget.amount) == 0 && exceeded == budget.exceeded &&
                Objects.equals(id, budget.id) && Objects.equals(title, budget.title) &&
                Objects.equals(user, budget.user) && Objects.equals(category, budget.category) &&
                Objects.equals(startDate, budget.startDate) && Objects.equals(endDate, budget.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, user, category, amount, startDate, endDate, exceeded);
    }
}
