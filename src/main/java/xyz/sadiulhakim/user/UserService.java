package xyz.sadiulhakim.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import xyz.sadiulhakim.exception.EntityNotFoundExecption;

@Service
public class UserService {

	private final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}
	
	public void save(User user) {

		LOGGER.info("Saving user {}", user.getEmail());
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		userRepository.save(user);
		LOGGER.info("Done saving user {}", user.getEmail());
	}

	public User findByEmail(String email) {
		
		LOGGER.info("Finding user by email {}", email);

		// Do not make an endPoint for this method as it displays the password
		// Do not remove the password here because that would be used by
		// AuthenticationProvider
		return userRepository.findByEmail(email)
				.orElseThrow(() -> new EntityNotFoundExecption("User id not found with email " + email));
	}

	public User findById(long id) {
		
		LOGGER.info("Finding user by id {}", id);

		var user = userRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundExecption("User id not found with id " + id));

		// Remove the password as there would be an end-point for this method
		// Do not display the password
		user.setPassword(null);
		return user;
	}
}
