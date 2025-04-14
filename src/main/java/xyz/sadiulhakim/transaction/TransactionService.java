package xyz.sadiulhakim.transaction;

import java.time.LocalDateTime;

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
}
