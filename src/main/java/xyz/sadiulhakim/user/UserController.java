package xyz.sadiulhakim.user;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
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
import xyz.sadiulhakim.exception.UnsupportedActivityException;

@RestController
@RequestMapping("/users")
public class UserController {
	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@RateLimiter(name = "defaultRateLimiter")
	@PostMapping("/register")
	ResponseEntity<Map<String, String>> registerUser(@RequestBody @Valid User user) {
		userService.save(user);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(Map.of("message", "User " + user.getEmail() + " is registered successfully."));
	}

	@RateLimiter(name = "defaultRateLimiter")
	@GetMapping
	ResponseEntity<List<User>> getUsers(@RequestParam(defaultValue = "0") int pageNumber,
			@RequestParam(defaultValue = "200") int pageSize) {

		if (pageSize > 200) {
			throw new UnsupportedActivityException("You can not load more than 200 items!");
		}
		return ResponseEntity.ok(userService.findAll(pageNumber, pageSize));
	}
	
	@RateLimiter(name = "defaultRateLimiter")
	@GetMapping("/{userId}")
	ResponseEntity<User> getUsers(@PathVariable long userId) {
		return ResponseEntity.ok(userService.findById(userId));
	}
	
	@RateLimiter(name = "defaultRateLimiter")
	@PostMapping("/{userId}")
	ResponseEntity<Map<String, String>> changePassword(@PathVariable long userId, @RequestBody @Valid ChangePasswordPojo pojo) {
		
		var res = userService.changePassword(userId, pojo);
		if(!res.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(Map.of("message",res));
		}
		
		return ResponseEntity.ok(Map.of("message", "Successfully changed password!"));
	}
	
	@RateLimiter(name = "defaultRateLimiter")
	@GetMapping("/{userId}/role/{roleId}")
	ResponseEntity<Map<String, String>> asignRole(@PathVariable long userId, @PathVariable long roleId){
		userService.assignRoleToUser(userId, roleId);
		return ResponseEntity.ok(Map.of("message", "User role is changed successfully!"));
	}
	
	@RateLimiter(name = "defaultRateLimiter")
	@DeleteMapping("/{userId}")
	ResponseEntity<Map<String, String>> deleteUsers(@PathVariable long userId) {
		userService.delete(userId);
		return ResponseEntity.ok(Map.of("message", "User is deleted successfully!"));
	}
}
