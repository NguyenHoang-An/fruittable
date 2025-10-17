package com.example.fruitables.product;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends MongoRepository<Product, String> {
    Optional<Product> findByslug(String slug);
    List<Product> findByEnabledTrue();
    List<Product> findByCategoryIgnoreCase(String category);
}
