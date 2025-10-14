package com.example.fruitables.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// Controller trả về các trang tĩnh (thymeleaf templates)
@Controller
public class PageController {
    // Trang chủ
    @GetMapping({"/","/index","/home"})
    public String index(){
        return "index";
    }

    // Trang chi tiết sản phẩm
    @GetMapping({"/shop-detail"})
    public String shopDetail(){
        return "shop-detail";
    }

    // Trang liên hệ
    @GetMapping({"/contact"})
    public String contact(){
        return "contact";
    }

    // Trang 404
    @GetMapping({"/404"})
    public String notFound(){
        return "404";
    }

    @GetMapping("/testimonial")
    public String testimonial() {
        return "testimonial"; // trỏ tới templates/testimonial.html
    }


}
