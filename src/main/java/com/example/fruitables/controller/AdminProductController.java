package com.example.fruitables.controller;

import com.example.fruitables.Service.FileStorageService;
import com.example.fruitables.product.Product;
import com.example.fruitables.product.ProductService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@PreAuthorize("hasRole('ADMIN')") //chỉ admin mới được truy cập
@Controller
@RequestMapping("/admin/products")
public class AdminProductController {
    private final ProductService productService;
    private final FileStorageService fileStorageService;

    public AdminProductController(ProductService productService,
                                  FileStorageService fileStorageService) {
        this.productService = productService;
        this.fileStorageService = fileStorageService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("items", productService.listAll());
        return "admin/product-list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("product", new Product());
        return "admin/product-form";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("product") Product product,
                         @RequestParam(value = "image", required = false) MultipartFile image,
                         BindingResult br) throws Exception {
        if (br.hasErrors()) return "admin/product-form";

        // ép giá trị an toàn
        if (product.getPrice() == null) product.setPrice(new BigDecimal("0"));
        if (image != null && !image.isEmpty()) {
            String url = fileStorageService.saveTo("/products", image);
            product.setImageUrl(url);
        }
        productService.save(product);
        return "redirect:/admin/products?created";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable String id, Model model) {
        Product p = productService.findById(id).orElse(null);
        if (p == null) return "redirect:/admin/products?notfound";
        model.addAttribute("product", p);
        return "admin/product-form";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable String id,
                         @ModelAttribute("product") Product incoming,
                         @RequestParam(value = "image", required = false) MultipartFile image,
                         BindingResult br) throws Exception {
        if (br.hasErrors()) return "admin/product-form";
        Product p = productService.findById(id).orElse(null);
        if (p == null) return "redirect:/admin/products?notfound";

        // cập nhật trường
        p.setName(incoming.getName());
        p.setDescription(incoming.getDescription());
        p.setPrice(incoming.getPrice());
        p.setUnit(incoming.getUnit());
        p.setCategory(incoming.getCategory());
        p.setEnabled(incoming.isEnabled());
        if (StringUtils.hasText(incoming.getSlug())) {
            p.setSlug(incoming.getSlug());
        }
        if (image != null && !image.isEmpty()) {
            String url = fileStorageService.saveTo("/products", image);
            p.setImageUrl(url);
        }
        productService.save(p);
        return "redirect:/admin/products?updated";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable String id) {
        productService.deleteById(id);
        return "redirect:/admin/products?deleted";
    }

}
