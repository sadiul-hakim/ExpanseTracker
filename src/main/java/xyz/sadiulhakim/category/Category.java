package xyz.sadiulhakim.category;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import xyz.sadiulhakim.user.User;

@Entity
public class Category {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(nullable = false, length = 55, unique = true)
	private String name;
	
	@Column(length = 200)
	private String decscription;
	
	@ManyToOne
	private User user;

	public Category() {
		super();
	}

	public Category(long id, @NotBlank @Size(max = 55) String name, @Size(max = 200) String decscription, User user) {
		super();
		this.id = id;
		this.name = name;
		this.decscription = decscription;
		this.user = user;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDecscription() {
		return decscription;
	}

	public void setDecscription(String decscription) {
		this.decscription = decscription;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public int hashCode() {
		return Objects.hash(decscription, id, name, user);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!(obj instanceof Category other)) return false;
		return id == other.id &&
			   Objects.equals(name, other.name) &&
			   Objects.equals(decscription, other.decscription) &&
			   Objects.equals(user, other.user);
	}
}
