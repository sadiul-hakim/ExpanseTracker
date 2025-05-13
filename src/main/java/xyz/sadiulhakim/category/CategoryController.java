package xyz.sadiulhakim.category;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/categories")
public class CategoryController {

	private final CategoryService service;

	public CategoryController(CategoryService service) {
		super();
		this.service = service;
	}

	@PostMapping
	ResponseEntity<Map<String, String>> createCategory(@RequestBody @Valid CategoryDTO dto) {
		service.save(dto);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(Map.of("message", "Category " + dto.name() + " is created successfully!"));
	}
}
