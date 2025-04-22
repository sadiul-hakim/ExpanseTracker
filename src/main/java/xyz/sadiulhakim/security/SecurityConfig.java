package xyz.sadiulhakim.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

@Configuration
public class SecurityConfig {
	private final CustomAuthorizationFilter customAuthorizationFilter;
	private final AuthenticationProvider authenticationProvider;

	public SecurityConfig(CustomAuthorizationFilter customAuthorizationFilter,
			AuthenticationProvider authenticationProvider) {
		this.customAuthorizationFilter = customAuthorizationFilter;
		this.authenticationProvider = authenticationProvider;
	}

	@Bean
	SecurityFilterChain config(HttpSecurity http) throws Exception {

		String[] permittedEndpoints = { "/login", "/refreshToken", "/validate-token","/ping" };
		String[] endpointsForAdmin = { "/role/**", "/users/**" };
		String[] endpointsForUser = { "/role/*", "/users/*" };

		return http
				.csrf(csrf -> csrf.ignoringRequestMatchers("/login", "/validate-token")
						.csrfTokenRepository(new CustomCsrfTokenRepository())
						.csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler()))
				.authorizeHttpRequests(auth -> auth.requestMatchers(permittedEndpoints).permitAll())
				.authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.GET, endpointsForUser).hasRole("USER"))
				.authorizeHttpRequests(auth -> auth.requestMatchers(endpointsForAdmin).hasRole("ADMIN"))
				.authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
				.authenticationProvider(authenticationProvider)
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.addFilterBefore(customAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
				.addFilter(new CustomAuthenticationFilter(authenticationProvider)).build();
	}
}
