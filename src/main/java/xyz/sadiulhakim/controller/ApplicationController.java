package xyz.sadiulhakim.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;

@RestController
public class ApplicationController {

	@RateLimiter(name = "defaultRateLimiter")
	@GetMapping("/ping")
	ResponseEntity<Map<String,String>> ping(){
		return ResponseEntity.ok(Map.of("message","UP and Running"));
	}
}
