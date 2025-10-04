package com.example.fruitables.controller;

import com.example.fruitables.Service.CurrentUserService;
import com.example.fruitables.Service.UserService;
import com.example.fruitables.order.OrderService;
import com.example.fruitables.order.OrderStatus;
import com.example.fruitables.user.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ProfileController {

    private final CurrentUserService currentUserService;
    private final UserService userService;
    private final OrderService orderService;

    // >>> Constructor injection: đảm bảo các field luôn được gán
    public ProfileController(CurrentUserService currentUserService,
                             UserService userService,
                             OrderService orderService) {
        this.currentUserService = currentUserService;
        this.userService = userService;
        this.orderService = orderService;
    }

    /**
     * Trang hồ sơ với lịch sử mua hàng
     * Query param: status = ALL | CONFIRMED | SHIPPING | DELIVERED
     */
    @GetMapping("/profile")
    public String profilePage(@RequestParam(name = "status", defaultValue = "ALL") String status,
                              Model model) {

        User user = currentUserService.getCurrentUser().orElse(null);
        if (user == null) {
            return "redirect:/login";
        }

        // Nhóm trạng thái đã mua
        List<OrderStatus> purchased = List.of(
                OrderStatus.CONFIRMED,
                OrderStatus.SHIPPING,
                OrderStatus.DELIVERED
        );

        // Lấy đơn theo trạng thái (chỉ cho phép trong nhóm purchased)
        List<?> orders;
        if ("ALL".equalsIgnoreCase(status)) {
            orders = orderService.findOrdersOf(user.getId(), purchased);
        } else {
            OrderStatus st;
            try {
                st = OrderStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException ex) {
                st = null;
            }
            if (st == null || !purchased.contains(st)) {
                orders = orderService.findOrdersOf(user.getId(), purchased);
                status = "ALL";
            } else {
                orders = orderService.findOrdersOf(user.getId(), st);
            }
        }

        // Bind model (sửa chính tả: "orders")
        model.addAttribute("orders", orders);
        model.addAttribute("status", status.toUpperCase());
        model.addAttribute("user", user);

        return "profile";
    }
}
