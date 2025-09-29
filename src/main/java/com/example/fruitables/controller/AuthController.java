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
    public String registerPage(Model model){
        model.addAttribute("registerDto", new RegisterDto());
        return "register";
    }

    // Xử lý submit đăng ký (POST)
    @PostMapping("/register")
    public String doRegister(@Valid @ModelAttribute ("registerDto") RegisterDto registerDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "register";
        }
        if(!registerDto.getPassword().equals(registerDto.getConfirm())){
            model.addAttribute("error", "Passwords do not match");
            return "register";
        }
        try {
            userService.register(registerDto.getFullname(), registerDto.getEmail(), registerDto.getUsername(), registerDto.getPassword());
            return "redirect:/login?registered";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage());
            return "register";
        }
    }

}

