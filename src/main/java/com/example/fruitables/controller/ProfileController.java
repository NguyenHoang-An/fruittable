package com.example.fruitables.controller;

import com.example.fruitables.Service.CurrentUserService;
import com.example.fruitables.user.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProfileController {
    private final CurrentUserService currentUserService;

    public ProfileController(CurrentUserService currentUserService){
        this.currentUserService = currentUserService;
    }

    @GetMapping("/profile")
    public String profile(Model model){
        User user = currentUserService.getCurrentUser()
                .orElseThrow(()-> new RuntimeException("User chưa đăng nhập"));
        model.addAttribute("user", user);
        return "profile";
    }
}
