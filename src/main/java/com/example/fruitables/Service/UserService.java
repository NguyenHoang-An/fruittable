package com.example.fruitables.Service;

import com.example.fruitables.user.*;
import java.util.*;
import java.util.stream.Collectors;

import com.example.fruitables.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

// Service chịu trách nhiệm nghiệp vụ người dùng và tích hợp Spring Security
@Service
public class UserService implements UserDetailsService {

    private final UserRepository repo;
    private final PasswordEncoder encoder;

    // Inject repository và password encoder
    @Autowired
    public UserService(UserRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    // Tải thông tin user cho Spring Security từ username
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User u = repo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new org.springframework.security.core.userdetails.User(
                u.getUsername(),
                u.getPassword(),
                u.isEnabled(),
                true, true, true,
                u.getRoles().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet())
        );
    }

    // Đăng ký người dùng mới: validate, mã hoá mật khẩu, gán quyền, lưu DB
    public void register(String fullname, String email, String username, String rawPassword) {
        if (repo.existsByUsername(username)) throw new IllegalArgumentException("Username already exists");
        if (repo.existsByEmail(email)) throw new IllegalArgumentException("Email already exists");

        User u = new User();
        u.setFullname(fullname);
        u.setEmail(email);
        u.setUsername(username);
        u.setPassword(encoder.encode(rawPassword));
        u.setRoles(Set.of("ROLE_USER"));
        // Lưu user vào MongoDB
        repo.save(u);
        // Ghi log đơn giản để theo dõi (có thể xem trên console)
        System.out.println("[REGISTER] Created user: " + u.getUsername() + " (" + u.getEmail() + ")");
    }
}
