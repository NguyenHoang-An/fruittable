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

    // Trang danh sách sản phẩm
    @GetMapping({"/shop"})
    public String shop(){
        return "shop";
    }

    // Trang chi tiết sản phẩm
    @GetMapping({"/shop-detail"})
    public String shopDetail(){
        return "shop-detail";
    }

    // Trang giỏ hàng
    @GetMapping({"/cart"})
    public String cart(){
        return "cart";
    }

    // Trang thanh toán
    @GetMapping({"/chackout"})
    public String chackout(){
        return "chackout";
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
}
