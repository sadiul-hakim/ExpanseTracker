package xyz.sadiulhakim.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import xyz.sadiulhakim.user.User;

public class CustomUserDetails implements UserDetails {

	private String username;
	private String password;
	private String role;
	
	public CustomUserDetails(User user) {
		this.username=user.getEmail();
		this.password=user.getPassword();
		this.role=user.getRole();
	}

	public CustomUserDetails(String username, String password, String role) {
		super();
		this.username = username;
		this.password = password;
		this.role = role;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority(role));
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

}
