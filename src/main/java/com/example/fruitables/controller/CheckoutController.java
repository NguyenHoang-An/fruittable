package com.example.fruitables.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CheckoutController {
    @GetMapping("/chackout")
    public String checkout() {
        return "chackout";
    }
}
