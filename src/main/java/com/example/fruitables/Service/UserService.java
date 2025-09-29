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
        System.out.println(">> [REGISTER] Request username=" + username + ", email=" + email);

        if (repo.existsByUsername(username)) {
            System.out.println(">> [REGISTER] Duplicate username: " + username);
            throw new IllegalArgumentException("Username already exists: " + username);
        }
        if (repo.existsByEmail(email)) {
            System.out.println(">> [REGISTER] Duplicate email: " + email);
            throw new IllegalArgumentException("Email already exists: " + email);
        }

        User u = new User();
        u.setFullname(fullname);
        u.setEmail(email);
        u.setUsername(username);
        u.setPassword(encoder.encode(rawPassword));

        // Sửa đúng chuẩn ROLE_
        u.setRoles(Set.of("ROLE_USER"));

        User saved = repo.save(u);
        System.out.println(">> [REGISTER] Created user id=" + saved.getId()
                + ", username=" + saved.getUsername() + ", email=" + saved.getEmail());
    }

}
