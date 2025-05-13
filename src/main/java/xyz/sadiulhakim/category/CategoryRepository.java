package xyz.sadiulhakim.category;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import xyz.sadiulhakim.user.User;

public interface CategoryRepository extends JpaRepository<Category, Long>{
	Optional<Category> findByName(String name);
	List<Category> findAllByUser(User user,Pageable page);
}
