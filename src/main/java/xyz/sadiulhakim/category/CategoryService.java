package xyz.sadiulhakim.category;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import xyz.sadiulhakim.exception.UnsupportedActivityException;
import xyz.sadiulhakim.user.UserService;

@Service
public class CategoryService {
    private final CategoryRepository repository;
    private final UserService userService;

    public CategoryService(CategoryRepository repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

    public List<CategoryDTO> findAllByUser(int pageNumber, int pageSize) {

        // Use the current userId
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userService.findByEmail(username);
        var list = repository.findAllByUser(user, PageRequest.of(pageNumber, pageSize));
        return list.stream().map(this::convertToDTO).toList();
    }

    // Should only be used inside the application.
    // Should not be called from any end point as this might expose users info.
    public Category findModelById(long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category is not found with id " + id));
    }

    // Should only be used inside the application.
    // Should not be called from any end point as this might expose users info.
    public Category findModelByName(String name) {
        return repository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Category is not found with name " + name));
    }

    public CategoryDTO findById(long id) {
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userService.findByEmail(username);

        var category = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category is not found with id " + id));

        if (!category.getUser().getEmail().equals(user.getEmail())) {
            throw new UnsupportedActivityException("You are not allowed to access this category");
        }

        return convertToDTO(category);
    }

    public CategoryDTO findByName(String name) {

        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userService.findByEmail(username);

        var category = repository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Category is not found with name " + name));

        if (!category.getUser().getEmail().equals(user.getEmail())) {
            throw new UnsupportedActivityException("You are not allowed to access this category");
        }

        return convertToDTO(category);
    }

    public CategoryDTO save(CategoryDTO category) {

        var oldCategory = repository.findById(category.id()).orElse(null);
        if (oldCategory == null) {
            oldCategory = new Category();

            // Use the current userId
            var username = SecurityContextHolder.getContext().getAuthentication().getName();
            var user = userService.findByEmail(username);
            oldCategory.setUser(user);
        }

        oldCategory.setName(category.name());
        oldCategory.setDecscription(category.description());
        var saved = repository.save(oldCategory);
        return convertToDTO(saved);
    }

    public void delete(long id) {

        // Use the current userId
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userService.findByEmail(username);
        var category = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category is not found with id " + id));
        if (!category.getUser().getEmail().equals(user.getEmail())) {
            throw new UnsupportedActivityException("You are not allowed to delete this category");
        }

        repository.delete(category);
    }

    public CategoryDTO convertToDTO(Category category) {
        return new CategoryDTO(category.getId(), category.getUser().getId(), category.getName(),
                category.getDecscription());
    }
}
