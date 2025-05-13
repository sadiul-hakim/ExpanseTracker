package xyz.sadiulhakim.category;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import xyz.sadiulhakim.exception.UnsupportedActivityException;

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

    @GetMapping
    ResponseEntity<List<CategoryDTO>> getCategories(@RequestParam(defaultValue = "0") int pageNumber,
                                                    @RequestParam(defaultValue = "200") int pageSize) {

        if (pageSize > 200) {
            throw new UnsupportedActivityException("You can not load more than 200 items at a time.");
        }
        return ResponseEntity.ok(service.findAllByUser(pageNumber, pageSize));
    }
}
