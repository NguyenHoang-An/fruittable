package com.example.fruitables.controller;

import com.example.fruitables.user.User;
import jakarta.validation.Valid;
import org.springframework.ui.Model;
import com.example.fruitables.Service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

// Controller xử lý login/register
@Controller
public class AuthController {
    private final UserService userService;
    public AuthController(UserService userService){
        this.userService = userService;
    }

    // Trang đăng nhập (GET)
    @GetMapping("/login")
    public String loginPage(){
        return "login";
    }

    // Trang đăng ký (GET)
    @GetMapping("/register")
    public String registerPage(){
        return "register";
    }

    // Xử lý submit đăng ký (POST)
    @PostMapping("/register")
    public String doRegister(@Valid @ModelAttribute User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "register";
        }
        try {
            userService.register(user.getFullname(), user.getEmail(), user.getUsername(), user.getPassword());
            return "redirect:/login?registered";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage());
            return "register";
        }
    }

}

