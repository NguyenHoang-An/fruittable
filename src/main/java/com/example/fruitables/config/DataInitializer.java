package com.example.fruitables.config;

import com.example.fruitables.user.User;
import com.example.fruitables.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Set;

@Configuration
public class DataInitializer {
    @Bean
    CommandLineRunner initAdmin(UserRepository users) {
        return args -> {
            String adminEmail = "admin@fruitables.local";
            users.findByEmail(adminEmail).orElseGet(() -> {
                User u = new User();
                u.setEmail(adminEmail);
                u.setFullname("Administrator");
                u.setPassword(new BCryptPasswordEncoder().encode("Admin@123"));
                u.setRoles(Set.of("ROLE_ADMIN")); // hoặc set<Enum> tùy model của bạn
                u.setEnabled(true);
                return users.save(u);
            });
        };
}
}
