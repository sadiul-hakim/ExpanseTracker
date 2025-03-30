package xyz.sadiulhakim.exception;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(EntityNotFoundExecption.class)
	ResponseEntity<Map<String,String>> handleEntityNotFoundExecption(EntityNotFoundExecption e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
	}
}
