package xyz.sadiulhakim.transaction;

import java.time.LocalDateTime;
import java.util.Objects;

import org.hibernate.annotations.Check;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import xyz.sadiulhakim.category.Category;
import xyz.sadiulhakim.user.User;

@Entity
@Check(constraints = "amount > 0")
public class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User user;

	@Column(nullable = false)
	private double amount;

	@Enumerated(EnumType.STRING)
	private TransactionType type;

	@Enumerated(EnumType.STRING)
	private Currency currency;
	private String description;
	
	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;

	private LocalDateTime time;

	public Transaction() {
	}

	public Transaction(User user, double amount, TransactionType type, Currency currency, String description,
			Category category, LocalDateTime time) {
		super();
		this.user = user;
		this.amount = amount;
		this.type = type;
		this.currency = currency;
		this.description = description;
		this.category = category;
		this.time = time;
	}

	public Transaction(Long id, User user, double amount, TransactionType type, Currency currency, String description,
			Category category, LocalDateTime time) {
		super();
		this.id = id;
		this.user = user;
		this.amount = amount;
		this.type = type;
		this.currency = currency;
		this.description = description;
		this.category = category;
		this.time = time;
	}


	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Long getId() {
		return id;
	}

	public User getUser() {
		return user;
	}

	public double getAmount() {
		return amount;
	}

	public TransactionType getType() {
		return type;
	}

	public Currency getCurrency() {
		return currency;
	}

	public LocalDateTime getTime() {
		return time;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public void setType(TransactionType type) {
		this.type = type;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public void setTime(LocalDateTime time) {
		this.time = time;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public int hashCode() {
		return Objects.hash(amount, category, currency, description, id, time, type, user);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Transaction other = (Transaction) obj;
		return Double.doubleToLongBits(amount) == Double.doubleToLongBits(other.amount)
				&& Objects.equals(category, other.category) && currency == other.currency
				&& Objects.equals(description, other.description) && Objects.equals(id, other.id)
				&& Objects.equals(time, other.time) && type == other.type && Objects.equals(user, other.user);
	}

}
