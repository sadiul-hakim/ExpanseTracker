package xyz.sadiulhakim.transaction;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record TransactionDTO(long userId, double amount, @NotNull @NotEmpty @NotBlank String type,
		@NotNull @NotEmpty @NotBlank String currency, LocalDateTime time) {
}
