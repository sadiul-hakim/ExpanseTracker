package xyz.sadiulhakim.listener;

import java.time.LocalDateTime;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import xyz.sadiulhakim.role.Role;
import xyz.sadiulhakim.role.RoleRepository;
import xyz.sadiulhakim.user.User;
import xyz.sadiulhakim.user.UserRepository;

@Component
public class ApplicationListener {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;

	public ApplicationListener(UserRepository userRepository, RoleRepository roleRepository,
			PasswordEncoder passwordEncoder) {
		super();
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Async("defaultTaskExecutor")
	@EventListener
	void serverStarted(WebServerInitializedEvent event) {
		System.out.println("Server is running on port : " + event.getWebServer().getPort());
	}

	@Async("defaultTaskExecutor")
	@EventListener
	void applicationReady(ApplicationReadyEvent event) {

		var adminRole = roleRepository.findByName("ROLE_ADMIN").orElse(null);
		if (adminRole == null) {
			roleRepository.save(new Role(null, "ROLE_ADMIN", null));
		}

		var hakim = userRepository.findByEmail("sadiulhakim@gmail.com").orElse(null);
		if (hakim == null) {

			var role = roleRepository.findByName("ROLE_ADMIN").orElse(null);
			var user = new User(null, "Sadiul Hakim", "sadiulhakim@gmail.com", "Hakim@123", role, LocalDateTime.now());
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			userRepository.save(user);
		}
	}
}
