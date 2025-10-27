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
    public UserService(UserRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    // ✅ Tải thông tin user cho Spring Security từ username hoặc email
    @Override
    public UserDetails loadUserByUsername(String loginInput) throws UsernameNotFoundException {
        // Tìm user theo username, nếu không có thì tìm theo email
        Optional<User> userOpt = repo.findByUsername(loginInput);
        if (userOpt.isEmpty()) {
            userOpt = repo.findByEmail(loginInput);
        }

        User user = userOpt.orElseThrow(() ->
                new UsernameNotFoundException("Không tìm thấy tài khoản: " + loginInput));

        // ✅ Nếu username bị null, fallback sang email
        String principal = user.getUsername() != null ? user.getUsername() : user.getEmail();
        if (principal == null) {
            throw new UsernameNotFoundException("Tài khoản không có username/email hợp lệ: " + loginInput);
        }

        // ✅ Trả về UserDetails hợp lệ cho Spring Security
        return org.springframework.security.core.userdetails.User.builder()
                .username(principal)
                .password(user.getPassword())
                .authorities(
                        user.getRoles().stream()
                                .map(r -> r.startsWith("ROLE_") ? r : "ROLE_" + r)
                                .map(SimpleGrantedAuthority::new)
                                .collect(Collectors.toList())
                )
                .disabled(!user.isEnabled())
                .build();
    }

    // ✅ Đăng ký người dùng mới: validate, mã hoá mật khẩu, gán quyền, lưu DB
    public void register(String fullName, String email, String username, String rawPassword) {
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
        u.setFullName(fullName);
        u.setEmail(email);
        u.setUsername(username);
        u.setPassword(encoder.encode(rawPassword));

        // Gán quyền mặc định cho người dùng mới
        u.setRoles(Set.of("ROLE_USER"));

        User saved = repo.save(u);
        System.out.println(">> [REGISTER] Created user id=" + saved.getId()
                + ", username=" + saved.getUsername() + ", email=" + saved.getEmail());
    }
}
