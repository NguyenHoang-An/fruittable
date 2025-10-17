package com.example.fruitables.controller;

import com.example.fruitables.order.Order;
import com.example.fruitables.order.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class OrderController {
    @Autowired
    private OrderRepository orderRepository;

    @GetMapping("/chackout")
    public String showcheckoutPage(Model model) {
        model.addAttribute("order", new Order());
        return "chackout";
    }

    @PostMapping("/chackout")
    public String processCheckout(@ModelAttribute Order order, Model model) {
        orderRepository.save(order);
        model.addAttribute("message", "Order placed successfully!");
        return "redirect:/index";
    }
}
