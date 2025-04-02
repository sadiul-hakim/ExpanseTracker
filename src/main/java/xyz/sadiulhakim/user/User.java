package xyz.sadiulhakim.user;

import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import xyz.sadiulhakim.role.Role;

@Entity
@Table(name = "application_user")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Size(min = 2, max = 55, message = "Name must be between 2 to 55 characters")
	@NotNull(message = "FullName can not be null.")
	@NotBlank(message = "FullName can not be blank.")
	@Column(length = 55, nullable = false)
	private String fullName;

	@Email(message = "Must be a valid mail")
	@NotNull(message = "Email can not be null.")
	@NotBlank(message = "Email can not be blank.")
	@Size(min = 10, max = 75, message = "Mail must be between 10 to 75 characters")
	@Column(length = 75, nullable = false, unique = true)
	private String email;

	@Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&]).{8,}$", message = "Password must be at least 8 characters long and include: \" +\r\n"
			+ "              \"1 uppercase letter, 1 lowercase letter, 1 number, and 1 special character (@$!%*?&).")
	@Size(min = 8, message = "Password must be at least 8 characters")
	@NotNull(message = "Password can not be null.")
	@NotBlank(message = "Password can not be blank.")
	@Column(length = 100, nullable = false)
	private String password;

	@ManyToOne
	private Role role;

	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime joiningDate;

	public User(Long id, @Size(min = 2, max = 55, message = "Name must be between 2 to 55 characters") String fullName,
			@Email(message = "Must be a valid mail") @Size(min = 10, max = 75, message = "Mail must be between 10 to 75 characters") String email,
			@Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&]).{8,}$", message = "Password must be at least 8 characters long and include: \" +\r\n"
					+ "              \"1 uppercase letter, 1 lowercase letter, 1 number, and 1 special character (@$!%*?&).") @Size(min = 8, message = "Password must be at least 8 characters") String password,
			Role role, LocalDateTime joiningDate) {
		super();
		this.id = id;
		this.fullName = fullName;
		this.email = email;
		this.password = password;
		this.role = role;
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

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
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
