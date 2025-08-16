package com.quora.app;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class QuoraAppApplication {

	public static void main(String[] args) {

		Dotenv dotenv = Dotenv.configure().load(); // Load environment variables from .env file

		dotenv.entries().
				forEach(entry -> System.setProperty(entry.getKey(), entry.getValue())); // Set system properties from environment variables and set them
		SpringApplication.run(QuoraAppApplication.class, args

		);
	}

}
