package xyz.sadiulhakim.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import xyz.sadiulhakim.exception_handler.CustomForbiddenExceptionHandler;
import xyz.sadiulhakim.refreshToken.RefreshTokenService;

import java.util.List;

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

        return http
                .csrf(csrf -> csrf.ignoringRequestMatchers("/login", "/auth/**", "/users/register")
                        .csrfTokenRepository(new CustomCsrfTokenRepository())
                        .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler()))
                .cors(cors -> {
                    CorsConfigurationSource source = request -> {
                        CorsConfiguration configuration = new CorsConfiguration();
                        configuration.setAllowedOrigins(List.of("*")); // TODO: Change it
                        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
                        configuration.setAllowedHeaders(List.of(
                                "Authorization",
                                "Cache-Control",
                                "Content-Type",
                                "X-Requested-With",
                                "Accept",
                                "Origin"
                        ));
                        return configuration;
                    };

                    cors.configurationSource(source);
                })
                .headers(headers -> headers
                        // X-Content-Type-Options: nosniff (added by default, you can skip this explicitly)
                        .contentTypeOptions(Customizer.withDefaults())

                        // X-Frame-Options: DENY (default by Spring Security, but you can explicitly set)
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::deny)

                        // Strict-Transport-Security for HTTPS (enable if HTTPS)
                        .httpStrictTransportSecurity(hsts -> hsts
                                .includeSubDomains(true)
                                .maxAgeInSeconds(31536000)
                        )
                )
                .authorizeHttpRequests(auth -> auth.requestMatchers(permittedEndpoints).permitAll())
                .authorizeHttpRequests(auth -> auth.requestMatchers(endpointsForAdmin).hasRole("ADMIN"))
                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                .exceptionHandling(ex -> ex.accessDeniedHandler(forbiddenExceptionHandler))
                .authenticationProvider(authenticationProvider)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(customAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilter(new CustomAuthenticationFilter(authenticationProvider, refreshTokenService)).build();
    }
}
