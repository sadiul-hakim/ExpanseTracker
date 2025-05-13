package xyz.sadiulhakim.transaction;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import xyz.sadiulhakim.category.Category;
import xyz.sadiulhakim.user.User;

import java.time.LocalDateTime;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

	// To find all the income or cost have been does by a user in a single currency
	// at a time
	List<Transaction> findByTimeAndCurrencyAndUserAndType(LocalDateTime time, Currency currency, User user,
			TransactionType type, Pageable page);

	// To find all the income or cost have been does by a user in a single currency
	// between a time range
	List<Transaction> findByTimeBetweenAndCurrencyAndUserAndType(LocalDateTime time1, LocalDateTime time2,
			Currency currency, User user, TransactionType type, Pageable page);

	List<Transaction> findByTimeBetweenAndCurrencyAndUserAndTypeAndCategory(LocalDateTime time1, LocalDateTime time2,
			Currency currency, User user, TransactionType type, Category category, Pageable page);

	// To find all the income or cost have been does by a user in a single currency
	List<Transaction> findAllByCurrencyAndUserAndType(Currency currency, User user, TransactionType type,
			Pageable page);

	// To find all the income or cost have been does by a user in a single currency
	List<Transaction> findAllByCurrencyAndUserAndTypeAndCategory(Currency currency, User user, TransactionType type,
			Category category, Pageable page);

	// To find all the income or cost have been does by a user
	// To find out which currencies did this user use
	List<Transaction> findAllByUserAndType(User user, TransactionType type, Pageable page);

	// All transactions made by a single user
	List<Transaction> findAllByUser(User user, Pageable page);
}
