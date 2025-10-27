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

    @Autowired
    @Lazy
    private UserService userService;

    // Bộ mã hoá mật khẩu dùng BCrypt
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Provider xác thực sử dụng UserService + PasswordEncoder
    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider p = new DaoAuthenticationProvider();
        p.setUserDetailsService(userService);
        p.setPasswordEncoder(passwordEncoder());
        return p;
    }

    // Chuỗi filter chính của Spring Security
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // ✅ CSRF: bỏ qua cho các trang public và tài nguyên tĩnh
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers(
                                "/login", "/register",
                                "/css/**", "/js/**", "/lib/**", "/img/**", "/images/**", "/fonts/**",
                                "/uploads/**", "/api/**"
                        )
                )

                // ✅ Provider xác thực
                .authenticationProvider(authProvider())

                // ✅ Phân quyền truy cập
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/", "/index", "/home", "/shop", "/shop-detail",
                                "/login", "/register", "/css/**", "/js/**", "/lib/**",
                                "/img/**", "/images/**", "/fonts/**", "/uploads/**",
                                "/cart", "/cart/**", "/404", "/testimonial", "/contact"
                        ).permitAll()
                        .requestMatchers("/profile", "/profile/edit", "/api/me").authenticated()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )

                // ✅ Form login
                .formLogin(login -> login
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .usernameParameter("login")
                        .passwordParameter("password")
                        .successHandler((request, response, authentication) -> {
                            // Chuyển hướng sau khi đăng nhập thành công
                            var roles = authentication.getAuthorities().toString();
                            if (roles.contains("ROLE_ADMIN")) {
                                response.sendRedirect("/admin/dashboard");
                            } else {
                                response.sendRedirect("/profile");
                            }
                        })
                        .failureUrl("/login?error")
                        .permitAll()
                )

                // ✅ Logout
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )

                // ✅ Remember me
                .rememberMe(rememberMe -> rememberMe
                        .key("uniqueAndSecret")
                        .tokenValiditySeconds(86400) // 1 ngày
                        .rememberMeParameter("remember-me")
                );
        return http.build();
    }

}

