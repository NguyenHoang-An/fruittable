package com.example.fruitables.user;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.*;

// Entity MongoDB lưu trong collection "users"
@Document(collection = "users")
public class User {
    // Khoá chính MongoDB
    @Id
    private String id;

    private String fullName;
    // Ràng buộc validate cơ bản
    @NotNull(message = "Username cannot be null")
    private String username;
    private String email;
    @Size(min = 8, message = "Password must be at least a characters")
    private String password;
    private String phone;
    private String address;
    private String avatarUrl;
    private boolean enabled = true; // bật/tắt tài khoản
    private Set<String> roles; // danh sách quyền (ROLE_*)
    private String gender;
    private LocalDate dob; // date of birth
    private LocalDate createdAt = LocalDate.now();


    public User() {}

    public User(String id, String email, String password){
        this.id = id;
        this.email = email;
        this.password = password;
    }

    // getters, setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullname) {
        this.fullName = fullname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }
}
