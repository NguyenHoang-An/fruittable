package com.example.fruitables.product;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.Normalizer;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository repo;

    public ProductService(ProductRepository repo) {
        this.repo = repo;
    }

    public List<Product> listActive() {
        return repo.findByEnabledTrue();
    }

    public List<Product> listAll() {
        return repo.findAll();
    }

    public Optional<Product> findById(String id) {
        return repo.findById(id);
    }

    public Optional<Product> findBySlug(String slug) {
        return repo.findByslug(slug);
    }

    public Product save(Product p) {
        // auto-gen slug nếu trống
        if (!StringUtils.hasText(p.getSlug()) && StringUtils.hasText(p.getName())) {
            p.setSlug(slugify(p.getName()));
        }
        p.setUpdatedAt(java.time.Instant.now());
        return repo.save(p);
    }

    public void deleteById(String id) {
        repo.deleteById(id);
    }

    private String slugify(String input) {
        String n = Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        n = n.toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-{2,}", "-")
                .replaceAll("^-|-$", "");
        return n;
    }
}
