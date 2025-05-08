package xyz.sadiulhakim.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.IOException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import xyz.sadiulhakim.pojo.Token;
import xyz.sadiulhakim.util.JwtHelper;

@RestController
public class TokenController {

	private final Logger LOGGER = LoggerFactory.getLogger(TokenController.class);
	private final UserDetailsService userDetailsService;

	public TokenController(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	@RateLimiter(name = "defaultRateLimiter")
	@GetMapping("/refreshToken")
	public ResponseEntity<Map<String, String>> refreshToken(HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (authorization == null || !authorization.startsWith("Bearer ")) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Refresh token missing"));
		}

		try {

			// Extract the token from authorization text
			String token = authorization.substring("Bearer ".length());

			// Extract the username
			String username = JwtHelper.extractUsername(token);

			// Get the userDetails using username
			UserDetails userDetails = userDetailsService.loadUserByUsername(username);

			// If the token is valid generate a new access token and send it to user.
			if (!JwtHelper.isValidToken(token, userDetails)) {
				throw new SecurityException("Invalid refresh token");
			}

			// Generate access token
			Map<String, Object> extraClaims = new HashMap<>();
			extraClaims.put("roles", userDetails.getAuthorities());
			String accessToken = JwtHelper.generateToken(userDetails, extraClaims, (1000 * 60 * 60 * 5));

			// Send it to the user
			Map<String, String> tokenMap = new HashMap<>();
			tokenMap.put("accessToken", accessToken);
			return ResponseEntity.ok(tokenMap);
		} catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException
				| IllegalArgumentException ex) {

			// If the token is Invalid send an error with the response
			Map<String, String> errorMap = new HashMap<>();
			errorMap.put("error", "Invalid Token");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMap);
		}
	}

	@RateLimiter(name = "defaultRateLimiter")
	@PostMapping(value = "/validate-token", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, String>> validateToken(@RequestBody Token token) {

		try {
			if (token.token().isEmpty()) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
						.body(Collections.singletonMap("error", "Invalid token"));
			}

			String username = JwtHelper.extractUsername(token.token());

			UserDetails userDetails = userDetailsService.loadUserByUsername(username);

			boolean validToken = JwtHelper.isValidToken(token.token(), userDetails);
			return validToken ? ResponseEntity.ok(Collections.singletonMap("message", "The Token is valid!"))
					: ResponseEntity.status(HttpStatus.BAD_REQUEST)
							.body(Collections.singletonMap("message", "Invalid token!"));
		} catch (Exception ex) {
			LOGGER.error(ex.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Collections.singletonMap("message", "Something went wrong!"));
		}
	}
}
