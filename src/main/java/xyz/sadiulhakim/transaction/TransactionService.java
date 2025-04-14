package xyz.sadiulhakim.transaction;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import xyz.sadiulhakim.user.UserService;

@Service
public class TransactionService {
	private final TransactionRepository repository;
	private final UserService userService;

	public TransactionService(TransactionRepository repository, UserService userService) {
		this.repository = repository;
		this.userService = userService;
	}

	public void save(TransactionDTO dto) {
		var user = userService.findById(dto.userId());
		var type = TransactionType.get(dto.type());
		var currency = Currency.get(dto.currency());
		var transaction = new Transaction(user, dto.amount(), type, currency, LocalDateTime.now());
		repository.save(transaction);
	}

	public List<Transaction> findAllTransactionsOfUser(long userId) {
		var user = userService.findById(userId);
		return repository.findAllByUser(user);
	}

	public List<Transaction> findAllTransactionsOfUserByType(long userId, String type) {
		var user = userService.findById(userId);
		var typeEnum = TransactionType.get(type);
		return repository.findAllByUserAndType(user, typeEnum);
	}

	public List<Transaction> findAllTransactionsOfUserByTypeAndCurrency(long userId, String type, String currency) {
		var user = userService.findById(userId);
		var typeEnum = TransactionType.get(type);
		var currencyEnum = Currency.get(currency);
		return repository.findAllByCurrencyAndUserAndType(currencyEnum, user, typeEnum);
	}

	public List<Transaction> findAllTransactionsOfUserByTypeAndCurrencyAtATime(long userId, String type,
			String currency, LocalDateTime time) {
		var user = userService.findById(userId);
		var typeEnum = TransactionType.get(type);
		var currencyEnum = Currency.get(currency);
		return repository.findByTimeAndCurrencyAndUserAndType(time, currencyEnum, user, typeEnum);
	}

	public List<Transaction> findAllTransactionsOfUserByTypeAndCurrencyBetweenTimes(long userId, String type,
			String currency, LocalDateTime time1, LocalDateTime time2) {
		var user = userService.findById(userId);
		var typeEnum = TransactionType.get(type);
		var currencyEnum = Currency.get(currency);
		return repository.findByTimeBetweenAndCurrencyAndUserAndType(time1, time2, currencyEnum, user, typeEnum);
	}
}
