package xyz.sadiulhakim.transaction;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/currency")
public class CurrencyController {
	
	@GetMapping("/dropdown")
	ResponseEntity<Map<String, Object>> currencyDropdown(){
		
		Map<String, Object> dropdown = new HashMap<>();
		for(var currency : Currency.values()) {
			dropdown.put("id", currency.getId());
			dropdown.put("symbol", currency.getSymbol());
			dropdown.put("code", currency.getIsoCode());
		}
		
		return ResponseEntity.ok(dropdown);
	}
}
