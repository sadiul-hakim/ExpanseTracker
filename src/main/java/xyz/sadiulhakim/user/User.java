package xyz.sadiulhakim.user;

import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "application_user")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Size(min = 2, max = 55, message = "Name must be between 2 to 55 characters")
	@Column(length = 55, nullable = false)
	private String fullName;

	@Email(message = "Must be a valid mail")
	@Size(min = 10, max = 75, message = "Mail must be between 10 to 75 characters")
	@Column(length = 75, nullable = false, unique = true)
	private String email;

	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "Password must be at least 8 characters long, include an uppercase letter, a lowercase letter, a number, and a special character.")
	@Size(min = 8, max = 16, message = "Password must be between 8 to 16 characters")
	@Column(length = 100, nullable = false)
	private String password;

	@Size(min = 6, max = 55, message = "Role must be between 6 to 55 characters")
	@Column(length = 55, nullable = false)
	private String role;

	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime joiningDate;

	public User(Long id, @Size(min = 2, max = 55, message = "Name must be between 2 to 55 characters") String fullName,
			@Email(message = "Must be a valid mail") @Size(min = 10, max = 75, message = "Mail must be between 10 to 75 characters") String email,
			@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "Password must be at least 8 characters long, include an uppercase letter, a lowercase letter, a number, and a special character.") @Size(min = 8, max = 16, message = "Password must be between 8 to 16 characters") String password,
			@Size(min = 6, max = 55, message = "Role must be between 6 to 55 characters") String role,
			LocalDateTime joiningDate) {
		super();
		this.id = id;
		this.fullName = fullName;
		this.email = email;
		this.password = password;
		this.joiningDate = joiningDate;
	}

	public User() {
		super();
	}

	public Long getId() {
		return id;
	}

	public String getFullName() {
		return fullName;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public LocalDateTime getJoiningDate() {
		return joiningDate;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setJoiningDate(LocalDateTime joiningDate) {
		this.joiningDate = joiningDate;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	@Override
	public int hashCode() {
		return Objects.hash(email, fullName, id, joiningDate, password, role);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return Objects.equals(email, other.email) && Objects.equals(fullName, other.fullName)
				&& Objects.equals(id, other.id) && Objects.equals(joiningDate, other.joiningDate)
				&& Objects.equals(password, other.password) && Objects.equals(role, other.role);
	}
}
