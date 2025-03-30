package xyz.sadiulhakim.user;

import org.springframework.stereotype.Service;

import xyz.sadiulhakim.exception.EntityNotFoundExecption;

@Service
public class UserService {
	private final UserRepository userRepository;

	public UserService(UserRepository userRepository) {
		super();
		this.userRepository = userRepository;
	}

	public User findByEmail(String email) {

		// Do not make an endPoint for this method as it displays the password
		// Do not remove the password here because that would be used by
		// AuthenticationProvider
		return userRepository.findByEmail(email)
				.orElseThrow(() -> new EntityNotFoundExecption("User id not found with email " + email));
	}

	public User findById(long id) {

		var user = userRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundExecption("User id not found with id " + id));
		
		// Remove the password as there would be an end-point for this method
		// Do not display the password
		user.setPassword(null);
		return user;
	}
}
