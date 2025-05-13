package xyz.sadiulhakim.transaction;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import xyz.sadiulhakim.category.CategoryService;
import xyz.sadiulhakim.exception.UnsupportedActivityException;
import xyz.sadiulhakim.user.UserService;

@Service
public class TransactionService {

	public static final String ROLE_ADMIN = "ROLE_ADMIN";

	private final TransactionRepository repository;
	private final UserService userService;
	private final CategoryService categoryService;

	public TransactionService(TransactionRepository repository, UserService userService,
			CategoryService categoryService) {
		this.repository = repository;
		this.userService = userService;
		this.categoryService = categoryService;
	}

	public void save(TransactionDTO dto) {

		// Use the current userId
		var username = SecurityContextHolder.getContext().getAuthentication().getName();
		var user = userService.findByEmail(username);

		var type = TransactionType.get(dto.type());
		var currency = Currency.get(dto.currency());

		var category = categoryService.findModelById(dto.categoryId());
		var transaction = new Transaction(user, dto.amount(), type, currency, dto.description(), category,
				LocalDateTime.now());
		repository.save(transaction);
	}

	public List<TransactionDTO> findAllTransactionsOfUser(int pageNumber, int pageSize) {

		// Use the current userId
		var username = SecurityContextHolder.getContext().getAuthentication().getName();
		var user = userService.findByEmail(username);
		var transactions = repository.findAllByUser(user, PageRequest.of(pageNumber, pageSize));
		return transactions.stream().map(this::convertToDto).toList();
	}

	public List<TransactionDTO> findAllTransactionsOfUserByType(String type, int pageNumber, int pageSize) {

		// Use the current userId
		var username = SecurityContextHolder.getContext().getAuthentication().getName();
		var user = userService.findByEmail(username);
		var typeEnum = TransactionType.get(type);
		var transactions = repository.findAllByUserAndType(user, typeEnum, PageRequest.of(pageNumber, pageSize));
		return transactions.stream().map(this::convertToDto).toList();
	}

	public List<TransactionDTO> findAllTransactionsOfUserByTypeAndCurrency(String type, String currency, int pageNumber,
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

	public List<TransactionDTO> findAllTransactionsOfUserByTypeAndCategoryAndCurrency(String category, String type,
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

	public List<TransactionDTO> findAllTransactionsOfUserByTypeAndCurrencyAtATime(String type, String currency,
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

	public List<TransactionDTO> findAllTransactionsOfUserByTypeAndCurrencyBetweenTimes(String type, String currency,
			LocalDateTime time1, LocalDateTime time2, int pageNumber, int pageSize) {

		if (time1 == null) {
			time1 = LocalDate.now().atStartOfDay();
		}

		if (time2 == null) {
			time2 = LocalDate.now().atTime(LocalTime.MAX);
		}

		// Use the current userId
		var username = SecurityContextHolder.getContext().getAuthentication().getName();
		var user = userService.findByEmail(username);
		var typeEnum = TransactionType.get(type);
		var currencyEnum = Currency.get(currency);
		var transactions = repository.findByTimeBetweenAndCurrencyAndUserAndType(time1, time2, currencyEnum, user,
				typeEnum, PageRequest.of(pageNumber, pageSize));
		return transactions.stream().map(this::convertToDto).toList();
	}

	public List<TransactionDTO> findAllTransactionsOfUserByTypeAndCategoryAndCurrencyBetweenTimes(String category,
			String type, String currency, LocalDateTime time1, LocalDateTime time2, int pageNumber, int pageSize) {

		if (time1 == null) {
			time1 = LocalDate.now().atStartOfDay();
		}

		if (time2 == null) {
			time2 = LocalDate.now().atTime(LocalTime.MAX);
		}

		// Use the current userId
		var username = SecurityContextHolder.getContext().getAuthentication().getName();
		var user = userService.findByEmail(username);
		var typeEnum = TransactionType.get(type);
		var currencyEnum = Currency.get(currency);
		var categoryModel = categoryService.findModelByName(category);
		var transactions = repository.findByTimeBetweenAndCurrencyAndUserAndTypeAndCategory(time1, time2, currencyEnum,
				user, typeEnum, categoryModel, PageRequest.of(pageNumber, pageSize));
		return transactions.stream().map(this::convertToDto).toList();
	}

	public TransactionDTO findById(long id) {

		var username = SecurityContextHolder.getContext().getAuthentication().getName();
		var user = userService.findByEmail(username);

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
	}

	private TransactionDTO convertToDto(Transaction trans) {
		return new TransactionDTO(trans.getUser().getId(), trans.getCategory().getId(), trans.getAmount(),
				trans.getType().getName(), trans.getCurrency().getIsoCode(), trans.getDescription(), trans.getTime());
	}
}
