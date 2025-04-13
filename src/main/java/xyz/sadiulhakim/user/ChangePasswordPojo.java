package xyz.sadiulhakim.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ChangePasswordPojo(
		@Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&]).{8,}$", message = "Password must be at least 8 characters long and include: \" +\r\n"
				+ "              \"1 uppercase letter, 1 lowercase letter, 1 number, and 1 special character (@$!%*?&).") @Size(min = 8, message = "Password must be at least 8 characters") @NotNull(message = "Password can not be null.") @NotBlank(message = "Password can not be blank.") String currentPassword,

		@Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&]).{8,}$", message = "Password must be at least 8 characters long and include: \" +\r\n"
				+ "              \"1 uppercase letter, 1 lowercase letter, 1 number, and 1 special character (@$!%*?&).") @Size(min = 8, message = "Password must be at least 8 characters") @NotNull(message = "Password can not be null.") @NotBlank(message = "Password can not be blank.") String newPassword,

		@Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&]).{8,}$", message = "Password must be at least 8 characters long and include: \" +\r\n"
				+ "              \"1 uppercase letter, 1 lowercase letter, 1 number, and 1 special character (@$!%*?&).") @Size(min = 8, message = "Password must be at least 8 characters") @NotNull(message = "Password can not be null.") @NotBlank(message = "Password can not be blank.") String confirmPassword) {

}
