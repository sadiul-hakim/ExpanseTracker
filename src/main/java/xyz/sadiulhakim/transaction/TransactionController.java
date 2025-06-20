package xyz.sadiulhakim.transaction;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.sadiulhakim.exception.UnsupportedActivityException;
import xyz.sadiulhakim.transaction.pojo.CategoryTypeSummery;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @RateLimiter(name = "defaultRateLimiter")
    @PostMapping
    ResponseEntity<Map<String, String>> saveTransaction(
            @RequestBody @Valid TransactionDTO dto) {
        transactionService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "Successfully saved transaction!"));
    }

    @RateLimiter(name = "defaultRateLimiter")
    @GetMapping("/get-by-id")
    ResponseEntity<TransactionDTO> findById(
            @RequestParam long id) {
        return ResponseEntity.ok(transactionService.findById(id));
    }

    @RateLimiter(name = "defaultRateLimiter")
    @DeleteMapping("/delete")
    ResponseEntity<Map<String, String>> delete(
            @RequestParam long id) {
        transactionService.delete(id);
        return ResponseEntity.ok(Map.of("message", "Successfully deleted transaction " + id + "."));
    }

    @RateLimiter(name = "defaultRateLimiter")
    @GetMapping
    ResponseEntity<List<TransactionDTO>> findAllByUser(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "200") int pageSize) {

        if (pageSize > 200) {
            throw new UnsupportedActivityException("You can not load more than 200 items!");
        }
        return ResponseEntity.ok(transactionService.findAllTransactionsOfUser(pageNumber, pageSize));
    }

    @RateLimiter(name = "defaultRateLimiter")
    @GetMapping("/type/{type}")
    ResponseEntity<List<TransactionDTO>> findAllByUserAndType(
            @PathVariable String type,
            @RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "200") int pageSize) {
        if (pageSize > 200) {
            throw new UnsupportedActivityException("You can not load more than 200 items!");
        }
        return ResponseEntity.ok(transactionService.findAllTransactionsOfUserByType(type, pageNumber, pageSize));
    }

    @RateLimiter(name = "defaultRateLimiter")
    @GetMapping("/type/{type}/currency/{currency}")
    ResponseEntity<List<TransactionDTO>> findAllByUserAndType(
            @PathVariable String type, @PathVariable String currency,
            @RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "200") int pageSize) {
        if (pageSize > 200) {
            throw new UnsupportedActivityException("You can not load more than 200 items!");
        }
        return ResponseEntity.ok(
                transactionService.findAllTransactionsOfUserByTypeAndCurrency(type, currency, pageNumber, pageSize));
    }

    @RateLimiter(name = "defaultRateLimiter")
    @GetMapping("/category/{category}/type/{type}/currency/{currency}")
    ResponseEntity<List<TransactionDTO>> findAllByUserAndTypeAndCategory(
            @PathVariable String category,
            @PathVariable String type, @PathVariable String currency, @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "200") int pageSize) {
        if (pageSize > 200) {
            throw new UnsupportedActivityException("You can not load more than 200 items!");
        }
        return ResponseEntity.ok(
                transactionService.findAllTransactionsOfUserByTypeAndCategoryAndCurrency(category, type, currency,
                        pageNumber, pageSize));
    }

    @RateLimiter(name = "defaultRateLimiter")
    @GetMapping("/type/{type}/currency/{currency}/on")
    ResponseEntity<List<TransactionDTO>> findAllByUserAndTypeOnADate(
            @PathVariable String type,
            @PathVariable String currency, @RequestParam LocalDateTime time,
            @RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "200") int pageSize) {
        if (pageSize > 200) {
            throw new UnsupportedActivityException("You can not load more than 200 items!");
        }
        return ResponseEntity.ok(transactionService.findAllTransactionsOfUserByTypeAndCurrencyAtATime(type, currency,
                time, pageNumber, pageSize));
    }

    @RateLimiter(name = "defaultRateLimiter")
    @GetMapping("/type/{type}/currency/{currency}/between")
    ResponseEntity<List<TransactionDTO>> findAllByUserAndTypeBetweenDates(
            @PathVariable String type,
            @PathVariable String currency, @RequestParam LocalDateTime time1, @RequestParam LocalDateTime time2,
            @RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "200") int pageSize) {
        if (pageSize > 200) {
            throw new UnsupportedActivityException("You can not load more than 200 items!");
        }

        long days = time1.until(time2, ChronoUnit.DAYS);
        if (days > 30) {
            throw new UnsupportedActivityException("You can not load more than 30 days transactions!");
        }

        return ResponseEntity.ok(transactionService.findAllTransactionsOfUserByTypeAndCurrencyBetweenTimes(type,
                currency, time1, time2, pageNumber, pageSize));
    }

    @RateLimiter(name = "defaultRateLimiter")
    @GetMapping("/category/{category}/type/{type}/currency/{currency}/between")
    ResponseEntity<List<TransactionDTO>> findAllByUserAndTypeAndCategoryBetweenDates(
            @PathVariable String category,
            @PathVariable String type, @PathVariable String currency, @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate, @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "200") int pageSize) {
        if (pageSize > 200) {
            throw new UnsupportedActivityException("You can not load more than 200 items!");
        }

        long days = startDate.until(endDate, ChronoUnit.DAYS);
        if (days > 30) {
            throw new UnsupportedActivityException("You can not load more than 30 days transactions!");
        }

        return ResponseEntity.ok(transactionService.findAllTransactionsOfUserByTypeAndCategoryAndCurrencyBetweenTimes(
                category, type, currency, startDate, endDate, pageNumber, pageSize));
    }

    @RateLimiter(name = "defaultRateLimiter")
    @GetMapping("/report")
    ResponseEntity<?> report(@RequestParam(defaultValue = "") LocalDateTime startDate,
                             @RequestParam(defaultValue = "") LocalDateTime endDate) {
        return ResponseEntity.ok(transactionService.report(startDate, endDate));
    }
}
