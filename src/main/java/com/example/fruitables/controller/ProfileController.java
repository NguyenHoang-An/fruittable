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
    public String profile(
            @RequestParam(name = "status", defaultValue = "ALL") String status,
            Model model) {

        // Chuẩn hoá trạng thái, chống null/rỗng rồi mới toUpperCase()
        String effectiveStatus = (status == null || status.isBlank())
                ? "ALL"
                : status.trim().toUpperCase();

        User user = currentUserService.getCurrentUser()
                .orElseThrow(() -> new RuntimeException("User chưa đăng nhập"));

        // Nhóm trạng thái đã mua
        List<OrderStatus> purchased = List.of(
                OrderStatus.CONFIRMED,
                OrderStatus.SHIPPING,
                OrderStatus.DELIVERED
        );

        // Lấy đơn theo trạng thái
        List<?> orders;
        if ("ALL".equals(effectiveStatus)) {
            orders = orderService.findOrdersOf(user.getId(), purchased);
        } else {
            OrderStatus st = null;
            try {
                st = OrderStatus.valueOf(effectiveStatus);
            } catch (IllegalArgumentException ignored) { /* giữ null */ }
            if (st == null || !purchased.contains(st)) {
                orders = orderService.findOrdersOf(user.getId(), purchased);
                effectiveStatus = "ALL";
            } else {
                orders = orderService.findOrdersOf(user.getId(), st);
            }
        }

        model.addAttribute("user", user);
        model.addAttribute("orders", orders);
        model.addAttribute("status", effectiveStatus);
        return "profile";
    }

}
