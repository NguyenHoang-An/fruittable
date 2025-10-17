package com.example.fruitables.controller;

import com.example.fruitables.product.Product;
import com.example.fruitables.product.ProductRepository;
import com.example.fruitables.product.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ShopController {
    private final ProductService productService;

    public ShopController(ProductService productService) {
        this.productService = productService;
    }

    //Trang chi tiết sản phẩm
    @GetMapping("/shop-detail/{id}")
    public String detailById(@PathVariable String id, Model model){
        Product p = productService.findById(id).orElse(null);
        if(p == null || !p.isEnabled()){
            return "redirect:/404";
        }
        model.addAttribute("product", p);
        return "shop-detail";
    }

    //Trang chi tiết sản phẩm theo slug
    @GetMapping("/p/{slug}")
    public String detailBySlug(@PathVariable String slug, Model model){
        Product p = productService.findBySlug(slug).orElse(null);
        if (p == null || !p.isEnabled()) {
            return "redirect:/404";
        }
        model.addAttribute("product", p);
        return "shop-detail";
    }
}
