package com.example.Climalert;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ClimalertApplication {
	public static void main(String[] args) {
		SpringApplication.run(ClimalertApplication.class, args);
	}
}