package xyz.sadiulhakim.config;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class ApplicationConfiguration {

	@Bean
	Executor defaultTaskExecutor() {
		return Executors.newVirtualThreadPerTaskExecutor();
	}
}
