package xyz.sadiulhakim.user;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import xyz.sadiulhakim.exception.UnsupportedActivityException;

@RestController
@RequestMapping("/users")
public class UserController {
	private final UserService userService;

	public UserController(UserService userService) {
		super();
		this.userService = userService;
	}

	@PostMapping
	ResponseEntity<Map<String, String>> registerUser(@RequestBody @Valid User user) {
		userService.save(user);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(Map.of("message", "User " + user.getEmail() + " is registered successfully."));
	}

	@GetMapping
	ResponseEntity<List<User>> getUsers(@RequestParam(defaultValue = "0") int pageNumber,
			@RequestParam(defaultValue = "200") int pageSize) {

		if (pageSize > 200) {
			throw new UnsupportedActivityException("You can not load more than 200 items!");
		}

		return ResponseEntity.ok(userService.findAll(pageNumber, pageSize));
	}
	
	@GetMapping("/{userId}")
	ResponseEntity<User> getUsers(@PathVariable long userId) {
		return ResponseEntity.ok(userService.findById(userId));
	}
}
