package com.example.fruitables.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class ProductController {
    @Autowired
    private ProductRepository productrepo;

    @GetMapping("/shop")
    public String shopPage(@RequestParam(required = false) String category, Model model) {
        List<Product> products;
        if (category != null && !category.isEmpty()) {
            products = productrepo.findByCategoryIgnoreCase(category);
        } else {
            products = productrepo.findAll();
        }

        // Đếm số lượng sản phẩm theo category
        Map<String, Long> categoryCounts = productrepo.findAll()
                .stream()
                .collect(Collectors.groupingBy(Product::getCategory, Collectors.counting()));

        model.addAttribute("products", products);
        model.addAttribute("categoryCounts", categoryCounts);
        return "shop";
    }

    @GetMapping("/shop-detail")
    public String detail(@PathVariable String id, Model model){
        Product p = productrepo.findById(id).orElse(null);
        model.addAttribute("product", p);
        return "shop-detail";
    }
}
