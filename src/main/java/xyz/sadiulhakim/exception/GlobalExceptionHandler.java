package xyz.sadiulhakim.exception;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

import io.jsonwebtoken.MalformedJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.jsonwebtoken.ExpiredJwtException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private static final String MESSAGE = "message";

    @ExceptionHandler(EntityNotFoundExecption.class)
    ResponseEntity<Map<String, String>> handleEntityNotFoundException(EntityNotFoundExecption e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(MESSAGE, e.getMessage()));
    }

    @ExceptionHandler(TokenExpiredException.class)
    ResponseEntity<Map<String, String>> handleTokenExpiredException(TokenExpiredException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(MESSAGE, e.getMessage()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    ResponseEntity<Map<String, String>> handleBadCredentialsException(BadCredentialsException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(MESSAGE, e.getMessage()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    ResponseEntity<Map<String, String>> handleAccessDeniedException(AccessDeniedException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of(MESSAGE, e.getMessage()));
    }

    @ExceptionHandler(MalformedJwtException.class)
    ResponseEntity<Map<String, String>> handleMalformedJwtException(MalformedJwtException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(MESSAGE, e.getMessage()));
    }

    @ExceptionHandler(UnsupportedOperationException.class)
    ResponseEntity<Map<String, String>> handleUnsupportedOperationException(UnsupportedOperationException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(MESSAGE, e.getMessage()));
    }

    @ExceptionHandler(RequestNotPermitted.class)
    ResponseEntity<Map<String, String>> handleRequestNotPermitted(RequestNotPermitted e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of(MESSAGE, "you have been blocked for making excessive calls!"));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public final ResponseEntity<Map<String, String>> handlerMethodArgumentValidExceptions(
            MethodArgumentNotValidException exception) {

        Map<String, String> errors = new HashMap<>();
        exception.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    ResponseEntity<Map<String, String>> handleExpiredJwtException(ExpiredJwtException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(MESSAGE, "Jwt token has been expired!"));
    }

    /*
     * Spring resolves exception handlers using a most-specific-first strategy. That means:
     *
     * If you have @ExceptionHandler(TokenExpiredException.class), and a TokenExpiredException is thrown,
     * that method will be invoked.
     *
     *If no handler matches the thrown exception specifically, Spring will walk up the class hierarchy until
     * it finds a suitable handler.
     *
     * If no match is found, and you have a generic @ExceptionHandler(Exception.class) — that will catch it.
     * */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericException(Exception e) {

        // Always log the full exception stacktrace for diagnostics
        LOGGER.error("Unhandled exception occurred", e); // use your logger
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(MESSAGE, "Something went wrong. Please try again later."));
    }

}