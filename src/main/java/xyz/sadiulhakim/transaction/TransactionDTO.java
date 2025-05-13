package xyz.sadiulhakim.transaction;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record TransactionDTO(long userId,long categoryId ,@Positive double amount, @NotNull @NotEmpty @NotBlank String type,
		@NotNull @NotEmpty @NotBlank String currency, @Size(max = 255) String description, LocalDateTime time) {
}
