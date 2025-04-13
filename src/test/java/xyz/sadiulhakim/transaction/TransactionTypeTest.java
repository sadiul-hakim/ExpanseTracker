package xyz.sadiulhakim.transaction;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TransactionTypeTest {

	@Test
	void getById() {
		var type = TransactionType.getById(1);
		assertEquals(TransactionType.INCOME, type);
	}
}
