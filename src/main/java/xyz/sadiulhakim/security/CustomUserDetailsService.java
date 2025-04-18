package xyz.sadiulhakim.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import xyz.sadiulhakim.user.UserService;

@Component
public class CustomUserDetailsService implements UserDetailsService {
	
	private final UserService userService;
	
	public CustomUserDetailsService(UserService userService) {
		this.userService = userService;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		var user = userService.findByEmail(username);
		return new CustomUserDetails(user);
	}

}
