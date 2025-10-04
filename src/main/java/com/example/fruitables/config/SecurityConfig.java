package com.example.fruitables.config;

import com.example.fruitables.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
// Cấu hình bảo mật Spring Security cho ứng dụng
public class SecurityConfig {

    private final UserService userService;

    // Inject qua constructor (giúp test tốt hơn và tránh circular dependency)
    @Autowired
    public SecurityConfig(@Lazy UserService userService) {
        this.userService = userService;
    }

    // Bộ mã hoá mật khẩu dùng BCrypt
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Provider xác thực sử dụng UserService + PasswordEncoder
    @Bean
    DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider p = new DaoAuthenticationProvider();
        p.setUserDetailsService(userService);
        p.setPasswordEncoder(passwordEncoder());
        return p;
    }

    // Chuỗi filter chính của Spring Security
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // CSRF: bỏ qua cho các endpoint tĩnh và trang public
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/login", "/register", "/css/**", "/js/**", "/lib/**", "/img/**", "/images/**", "/fonts/**"))

                // Provider xác thực tuỳ chỉnh
                .authenticationProvider(authProvider())

                // Phân quyền truy cập
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/index", "/home", "/shop", "/shop-detail", "/login", "/register", "/css/**", "/js/**", "/lib/**", "/img/**", "/images/**", "/fonts/**","/uploads/**","/cart","/cart/**")
                        .permitAll()  // Cho phép truy cập công khai
                        .requestMatchers("/profile","/profile/edit","/api/me").authenticated()
                        .anyRequest().authenticated()  // Còn lại yêu cầu đăng nhập
                )
                // Cấu hình trang đăng nhập
                .formLogin(login -> login
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/", true)
                        .defaultSuccessUrl("/profile", true)
                        .failureUrl("/login?error")
                        .permitAll()
                )
                // Đăng xuất
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
                // Ghi nhớ đăng nhập (remember-me)
                .rememberMe(rememberMe -> rememberMe
                        .key("uniqueAndSecret")
                        .tokenValiditySeconds(86400)
                        .rememberMeParameter("remember-me")
                )
                // CSRF: KHÔNG ignore /register (để buộc phải có token)
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/**") // nếu có API thuần JSON
                );

        return http.build();
    }
}

