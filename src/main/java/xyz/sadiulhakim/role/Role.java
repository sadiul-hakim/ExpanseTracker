package xyz.sadiulhakim.role;

import java.util.Objects;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class Role {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 45, nullable = false)
	@NotNull(message = "Name can not be null.")
	@NotBlank(message = "Name can not be blank.")
	@Size(min = 6, max = 45, message = "name must be between 6 to 45 characters")
	private String name;

	@Column(length = 45)
	@Nullable
	@Size(max = 100, message = "description must be between 20 to 100 characters")
	private String description;

	public Role() {
		super();
	}

	public Role(Long id, @Size(min = 6, max = 45, message = "name must be between 6 to 45 characters") String name,
			@Size(min = 20, max = 100, message = "description must be between 20 to 100 characters") String description) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public int hashCode() {
		return Objects.hash(description, id, name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Role other = (Role) obj;
		return Objects.equals(description, other.description) && Objects.equals(id, other.id)
				&& Objects.equals(name, other.name);
	}
}
