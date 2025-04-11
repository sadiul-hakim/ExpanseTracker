package xyz.sadiulhakim.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import io.github.resilience4j.ratelimiter.RequestNotPermitted;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(EntityNotFoundExecption.class)
	ResponseEntity<Map<String, String>> handleEntityNotFoundExecption(EntityNotFoundExecption e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
	}
	
	@ExceptionHandler(UnsupportedOperationException.class)
	ResponseEntity<Map<String, String>> handleUnsupportedOperationException(UnsupportedOperationException e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
	}
	
	@ExceptionHandler(RequestNotPermitted.class)
	ResponseEntity<Map<String, String>> handleRequestNotPermitted(RequestNotPermitted e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "you have been blocked for making excessive calls!"));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public final ResponseEntity<Map<String, String>> handlerMethodArgumentValidExceptions(
			MethodArgumentNotValidException exception) {

		Map<String, String> errors = new HashMap<>();
		exception.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
		return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
	}
}