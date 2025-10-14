package com.example.fruitables.user;

import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.*;
// Repository MongoDB cho entity User, cung cấp CRUD và các truy vấn tuỳ chỉnh
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
    void deleteByEmail(String email);
    List<User> findTop10ByOrderByCreatedAtDesc();

}
