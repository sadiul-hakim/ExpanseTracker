package xyz.sadiulhakim.transaction;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import xyz.sadiulhakim.exception.UnsupportedActivityException;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

	private final TransactionService transactionService;

	public TransactionController(TransactionService transactionService) {
		this.transactionService = transactionService;
	}

	@PostMapping
	ResponseEntity<Map<String, String>> saveTransaction(@RequestBody @Valid TransactionDTO dto) {
		transactionService.save(dto);
		return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "Successfully saved transaction!"));
	}

	@GetMapping("/get-by-id")
	ResponseEntity<TransactionDTO> findById(@RequestParam long id) {
		return ResponseEntity.ok(transactionService.findById(id));
	}

	@DeleteMapping("/delete")
	ResponseEntity<Map<String, String>> delete(@RequestParam long id) {
		transactionService.delete(id);
		return ResponseEntity.ok(Map.of("message", "Successfully deleted transaction " + id + "."));
	}

	@GetMapping
	ResponseEntity<List<TransactionDTO>> findAllByUser(@RequestParam(defaultValue = "0") int pageNumber,
			@RequestParam(defaultValue = "200") int pageSize) {

		if (pageSize > 200) {
			throw new UnsupportedActivityException("You can not load more than 200 items!");
		}
		return ResponseEntity.ok(transactionService.findAllTransactionsOfUser(pageNumber, pageSize));
	}

	@GetMapping("/type/{type}")
	ResponseEntity<List<TransactionDTO>> findAllByUserAndType(@PathVariable String type,
			@RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "200") int pageSize) {
		if (pageSize > 200) {
			throw new UnsupportedActivityException("You can not load more than 200 items!");
		}
		return ResponseEntity.ok(transactionService.findAllTransactionsOfUserByType(type, pageNumber, pageSize));
	}

	@GetMapping("/type/{type}/currency/{currency}")
	ResponseEntity<List<TransactionDTO>> findAllByUserAndType(@PathVariable String type, @PathVariable String currency,
			@RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "200") int pageSize) {
		if (pageSize > 200) {
			throw new UnsupportedActivityException("You can not load more than 200 items!");
		}
		return ResponseEntity.ok(
				transactionService.findAllTransactionsOfUserByTypeAndCurrency(type, currency, pageNumber, pageSize));
	}

	@GetMapping("/type/{type}/currency/{currency}/on")
	ResponseEntity<List<TransactionDTO>> findAllByUserAndTypeOnADate(@PathVariable String type,
			@PathVariable String currency, @RequestParam LocalDateTime time,
			@RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "200") int pageSize) {
		if (pageSize > 200) {
			throw new UnsupportedActivityException("You can not load more than 200 items!");
		}
		return ResponseEntity.ok(transactionService.findAllTransactionsOfUserByTypeAndCurrencyAtATime(type, currency,
				time, pageNumber, pageSize));
	}

	@GetMapping("/type/{type}/currency/{currency}/between")
	ResponseEntity<List<TransactionDTO>> findAllByUserAndTypeBetweenDates(@PathVariable String type,
			@PathVariable String currency, @RequestParam LocalDateTime time1, @RequestParam LocalDateTime time2,
			@RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "200") int pageSize) {
		if (pageSize > 200) {
			throw new UnsupportedActivityException("You can not load more than 200 items!");
		}
		return ResponseEntity.ok(transactionService.findAllTransactionsOfUserByTypeAndCurrencyBetweenTimes(type,
				currency, time1, time2, pageNumber, pageSize));
	}
}
