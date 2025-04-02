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
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/role")
public class RoleController {

	private final RoleService roleService;

	public RoleController(RoleService roleService) {
		this.roleService = roleService;
	}

	@PostMapping
	ResponseEntity<Map<String, String>> saveRole(@RequestBody @Valid Role role) {
		roleService.save(role);
		return ResponseEntity.ok(Map.of("message", "Successfully saved role " + role.getName()));
	}

	@GetMapping
	ResponseEntity<List<Role>> findAllRoles() {
		var all = roleService.findAll();
		return ResponseEntity.ok(all);
	}

	@GetMapping("/{id}")
	ResponseEntity<Role> findById(@PathVariable long id) {
		var role = roleService.findById(id);
		return ResponseEntity.ok(role);
	}

	@DeleteMapping("/{id}")
	ResponseEntity<Map<String, String>> deleteById(@PathVariable long id) {
		roleService.delete(id);
		return ResponseEntity.ok(Map.of("message", "Successfully deleted role " + id));
	}
}
