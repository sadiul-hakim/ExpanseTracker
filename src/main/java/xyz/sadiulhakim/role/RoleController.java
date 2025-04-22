package xyz.sadiulhakim.role;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/role")
public class RoleController {

	private final RoleService roleService;

	public RoleController(RoleService roleService) {
		this.roleService = roleService;
	}

	@RateLimiter(name = "defaultRateLimiter")
	@PostMapping
	ResponseEntity<Map<String, String>> saveRole(@RequestBody @Valid Role role) {
		roleService.save(role);
		return ResponseEntity.ok(Map.of("message", "Successfully saved role " + role.getName()));
	}

	@RateLimiter(name = "defaultRateLimiter")
	@GetMapping
	ResponseEntity<List<Role>> findAllRoles(@RequestParam(defaultValue = "0") int pageNumber,
			@RequestParam(defaultValue = "200") int pageSize) {
		var all = roleService.findAll(pageNumber,pageSize);
		return ResponseEntity.ok(all);
	}

	@RateLimiter(name = "defaultRateLimiter")
	@GetMapping("/{id}")
	ResponseEntity<Role> findById(@PathVariable long id) {
		var role = roleService.findById(id);
		return ResponseEntity.ok(role);
	}

	@RateLimiter(name = "defaultRateLimiter")
	@DeleteMapping("/{id}")
	ResponseEntity<Map<String, String>> deleteById(@PathVariable long id) {
		roleService.delete(id);
		return ResponseEntity.ok(Map.of("message", "Successfully deleted role " + id));
	}
}
