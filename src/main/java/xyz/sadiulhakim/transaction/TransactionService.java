package xyz.sadiulhakim.transaction;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import xyz.sadiulhakim.budget.Budget;
import xyz.sadiulhakim.budget.BudgetService;
import xyz.sadiulhakim.budget.pojo.BudgetDTO;
import xyz.sadiulhakim.category.CategoryService;
import xyz.sadiulhakim.event.BudgetEvent;
import xyz.sadiulhakim.exception.UnsupportedActivityException;
import xyz.sadiulhakim.transaction.pojo.CategoryTypeSummery;
import xyz.sadiulhakim.transaction.pojo.TypeSummery;
import xyz.sadiulhakim.user.UserService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TransactionService {

    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionService.class);

    private final TransactionRepository repository;
    private final UserService userService;
    private final CategoryService categoryService;
    private final BudgetService budgetService;
    private final ApplicationEventPublisher eventPublisher;

    public TransactionService(TransactionRepository repository, UserService userService,
                              CategoryService categoryService, BudgetService budgetService,
                              ApplicationEventPublisher eventPublisher) {
        this.repository = repository;
        this.userService = userService;
        this.categoryService = categoryService;
        this.budgetService = budgetService;
        this.eventPublisher = eventPublisher;
    }

    public void save(TransactionDTO dto) {

        try {
            // Use the current userId
            var username = SecurityContextHolder.getContext().getAuthentication().getName();
            var user = userService.findByEmail(username);
            var category = categoryService.findModelById(dto.categoryId());
            var type = TransactionType.get(dto.type());

            LocalDateTime now = LocalDateTime.now();

            // Check for Budget
            if (type.equals(TransactionType.COST)) {

                // If current date is between budgets start and end date, the budget is still active.
                List<Budget> activeBudgets = budgetService.findActiveBudgetsForUserAndCategory(category, now, now);
                for (Budget activeBudget : activeBudgets) {
                    double cost = repository.sumAmountByUserAndCategoryAndTimeBetweenAndType(user, category,
                            activeBudget.getStartDate(), activeBudget.getEndDate(), TransactionType.COST);
                    double percentage = (cost / activeBudget.getAmount()) * 100;
                    if (percentage < 90) {
                        continue;
                    }
                    eventPublisher.publishEvent(new BudgetEvent(username, cost, activeBudget, percentage));
                }
            }

            var currency = Currency.get(dto.currency());
            var transaction = new Transaction(user, dto.amount(), type, currency, dto.description(), category, now);
            repository.save(transaction);
        } catch (Exception ex) {
            LOGGER.error("Failed to save transaction of user {} of category {} of amount {}", dto.userId(),
                    dto.categoryId(), dto.amount());
        }
    }

    public List<TransactionDTO> findAllTransactionsOfUser(int pageNumber, int pageSize) {

        // Use the current userId
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userService.findByEmail(username);
        var transactions = repository.findAllByUser(user, PageRequest.of(pageNumber, pageSize));
        return transactions.stream().map(this::convertToDto).toList();
    }

    public List<TransactionDTO> findAllTransactionsOfUserByType(
            String type, int pageNumber, int pageSize) {

        // Use the current userId
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userService.findByEmail(username);
        var typeEnum = TransactionType.get(type);
        var transactions = repository.findAllByUserAndType(user, typeEnum, PageRequest.of(pageNumber, pageSize));
        return transactions.stream().map(this::convertToDto).toList();
    }

    public List<TransactionDTO> findAllTransactionsOfUserByTypeAndCurrency(
            String type, String currency, int pageNumber,
            int pageSize) {

        // Use the current userId
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userService.findByEmail(username);
        var typeEnum = TransactionType.get(type);
        var currencyEnum = Currency.get(currency);
        var transactions = repository.findAllByCurrencyAndUserAndType(currencyEnum, user, typeEnum,
                PageRequest.of(pageNumber, pageSize));
        return transactions.stream().map(this::convertToDto).toList();
    }

    public List<TransactionDTO> findAllTransactionsOfUserByTypeAndCategoryAndCurrency(
            String category, String type,
            String currency, int pageNumber, int pageSize) {

        // Use the current userId
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userService.findByEmail(username);
        var typeEnum = TransactionType.get(type);
        var currencyEnum = Currency.get(currency);
        var categoryModel = categoryService.findModelByName(category);

        var transactions = repository.findAllByCurrencyAndUserAndTypeAndCategory(currencyEnum, user, typeEnum,
                categoryModel, PageRequest.of(pageNumber, pageSize));
        return transactions.stream().map(this::convertToDto).toList();
    }

    public List<TransactionDTO> findAllTransactionsOfUserByTypeAndCurrencyAtATime(
            String type, String currency,
            LocalDateTime time, int pageNumber, int pageSize) {

        if (time == null) {
            time = LocalDateTime.now();
        }

        // Use the current userId
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userService.findByEmail(username);
        var typeEnum = TransactionType.get(type);
        var currencyEnum = Currency.get(currency);
        var transactions = repository.findByTimeAndCurrencyAndUserAndType(time, currencyEnum, user, typeEnum,
                PageRequest.of(pageNumber, pageSize));
        return transactions.stream().map(this::convertToDto).toList();
    }

    public List<TransactionDTO> findAllTransactionsOfUserByTypeAndCurrencyBetweenTimes(
            String type, String currency,
            LocalDateTime startDate, LocalDateTime endDate, int pageNumber, int pageSize) {

        if (startDate == null) {
            startDate = LocalDate.now().atStartOfDay();
        }

        if (endDate == null) {
            endDate = LocalDate.now().atTime(LocalTime.MAX);
        }

        // Use the current userId
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userService.findByEmail(username);
        var typeEnum = TransactionType.get(type);
        var currencyEnum = Currency.get(currency);
        var transactions = repository.findByTimeBetweenAndCurrencyAndUserAndType(startDate, endDate, currencyEnum, user,
                typeEnum, PageRequest.of(pageNumber, pageSize));
        return transactions.stream().map(this::convertToDto).toList();
    }

    public List<TransactionDTO> findAllTransactionsOfUserByTypeAndCategoryAndCurrencyBetweenTimes(
            String category,
            String type, String currency, LocalDateTime startDate, LocalDateTime endDate, int pageNumber, int pageSize) {

        if (startDate == null) {
            startDate = LocalDate.now().atStartOfDay();
        }

        if (endDate == null) {
            endDate = LocalDate.now().atTime(LocalTime.MAX);
        }

        // Use the current userId
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userService.findByEmail(username);
        var typeEnum = TransactionType.get(type);
        var currencyEnum = Currency.get(currency);
        var categoryModel = categoryService.findModelByName(category);
        var transactions = repository.findByTimeBetweenAndCurrencyAndUserAndTypeAndCategory(startDate, endDate, currencyEnum,
                user, typeEnum, categoryModel, PageRequest.of(pageNumber, pageSize));
        return transactions.stream().map(this::convertToDto).toList();
    }

    public TransactionDTO findById(long id) {

        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userService.findByEmail(username);
        LOGGER.info("Getting transaction {} by id of user {}", id, username);

        var trans = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Transaction is not found with id " + id));

        if (!trans.getUser().getId().equals(user.getId()) && !user.getRole().getName().equals(ROLE_ADMIN)) {
            throw new UnsupportedActivityException("You are not allowed to access the transaction!");
        }

        return convertToDto(trans);
    }

    public void delete(long id) {

        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userService.findByEmail(username);

        var trans = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Transaction is not found with id " + id));

        if (!trans.getUser().getId().equals(user.getId()) && !user.getRole().getName().equals(ROLE_ADMIN)) {
            throw new UnsupportedActivityException("You are not allowed to delete this transaction!");
        }

        repository.delete(trans);
        LOGGER.info("Done deleting transaction {} of user {}", id, user.getEmail());
    }

    private TransactionDTO convertToDto(Transaction trans) {
        return new TransactionDTO(trans.getUser().getId(), trans.getCategory().getId(), trans.getAmount(),
                trans.getType().getName(), trans.getCurrency().getIsoCode(), trans.getDescription(), trans.getTime());
    }

    public Map<String, Object> report(LocalDateTime startDate, LocalDateTime endDate) {

        LocalDateTime today = LocalDateTime.now();
        if (startDate == null) {
            startDate = today.withDayOfMonth(1);
        }

        if (endDate == null) {
            endDate = today.with(TemporalAdjusters.lastDayOfMonth());
        }

        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userService.findByEmail(username);

        List<CategoryTypeSummery> categoryWise = repository.getSummedAmountsByUserCategoryAndType(
                user, startDate, endDate);
        List<TypeSummery> typeWise = repository.getSummedAmountByUserType(user, startDate, endDate);
        List<BudgetDTO> budgets = budgetService.findAll();

        Map<String, Object> report = new HashMap<>();
        report.put("summedByCategory", categoryWise);
        report.put("summedByType", typeWise);
        report.put("budgets", budgets);
        return report;
    }
}
