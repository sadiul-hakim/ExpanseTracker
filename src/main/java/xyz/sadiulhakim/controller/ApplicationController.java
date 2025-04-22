package xyz.sadiulhakim.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApplicationController {

	@GetMapping("/ping")
	ResponseEntity<Map<String,String>> ping(){
		return ResponseEntity.ok(Map.of("message","UP and Running"));
	}
}
