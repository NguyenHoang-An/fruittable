package com.example.fruitables;

import com.example.fruitables.user.User;
import com.example.fruitables.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

// Điểm khởi động của ứng dụng Spring Boot
@SpringBootApplication
public class FruitablesApplication {

	public static void main(String[] args) {
		// Chạy ứng dụng Spring Boot
		SpringApplication.run(FruitablesApplication.class, args);
	}
}
