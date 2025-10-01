package com.example.fruitables.controller;

import com.example.fruitables.Service.CartSession;
import com.example.fruitables.model.CartItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class CartController {
    private final CartSession cart;

    @GetMapping("/cart")
    public String viewCart(Model model){
        model.addAttribute("cartItems", cart.getItems());
        model.addAttribute("subtotal", cart.getSubtotal());
        model.addAttribute("shipping", cart.getShipping());
        model.addAttribute("total", cart.getTotal());
        return "cart";
    }

    @PostMapping("/cart/add")
    public String addToCart(@RequestParam Long productId, @RequestParam String name,
                            @RequestParam String image, @RequestParam long unitPrice,
                            @RequestParam(defaultValue = "1") int quantity){
        CartItem item = new CartItem();
        item.setProductId(productId);
        item.setName(name);
        item.setImage(image);
        item.setUnitPrice(unitPrice);
        item.setQuantity(quantity);
        cart.add(item);
        return "redirect:/cart";
    }

    @PostMapping("/cart/update")
    public String updateQty(@RequestParam Long productId, @RequestParam int quantity){
        cart.update(productId, quantity);
        return "redirect:/cart";
    }

    @PostMapping("/cart/remove")
    public String remove(@RequestParam Long productId){
        cart.remove(productId);
        return "redirect:/cart";
    }
}
