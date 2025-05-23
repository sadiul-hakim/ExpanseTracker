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

import xyz.sadiulhakim.exception_handler.CustomForbiddenExceptionHandler;
import xyz.sadiulhakim.refreshToken.RefreshTokenService;

@Configuration
public class SecurityConfig {
    private final CustomAuthorizationFilter customAuthorizationFilter;
    private final AuthenticationProvider authenticationProvider;
    private final RefreshTokenService refreshTokenService;
    private final CustomForbiddenExceptionHandler forbiddenExceptionHandler;

    public SecurityConfig(CustomAuthorizationFilter customAuthorizationFilter,
                          AuthenticationProvider authenticationProvider, RefreshTokenService refreshTokenService,
                          CustomForbiddenExceptionHandler forbiddenExceptionHandler) {
        this.customAuthorizationFilter = customAuthorizationFilter;
        this.authenticationProvider = authenticationProvider;
        this.refreshTokenService = refreshTokenService;
        this.forbiddenExceptionHandler = forbiddenExceptionHandler;
    }

    @Bean
    SecurityFilterChain config(HttpSecurity http) throws Exception {

        String[] permittedEndpoints = {"/login", "/auth/**", "/refresh-token/**", "/users/register"};
        String[] endpointsForAdmin = {"/role/**", "/users/**"};
        String[] endpointsForUser = {"/role/*", "/users/*"}; // TODO: Fix this

        return http
                .csrf(csrf -> csrf.ignoringRequestMatchers("/login", "/auth/**", "/users/register")
                        .csrfTokenRepository(new CustomCsrfTokenRepository())
                        .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler()))
                .authorizeHttpRequests(auth -> auth.requestMatchers(permittedEndpoints).permitAll())
                .authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.GET, endpointsForUser).hasRole("USER"))
                .authorizeHttpRequests(auth -> auth.requestMatchers(endpointsForAdmin).hasRole("ADMIN"))
                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                .exceptionHandling(ex -> ex.accessDeniedHandler(forbiddenExceptionHandler))
                .authenticationProvider(authenticationProvider)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(customAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilter(new CustomAuthenticationFilter(authenticationProvider, refreshTokenService)).build();
    }
}
