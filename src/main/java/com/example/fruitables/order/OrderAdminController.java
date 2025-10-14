package com.example.fruitables.order;

import com.example.fruitables.admin.RevenueBroadcaster;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.time.LocalDate;

@PreAuthorize("hasRole('ADMIN')")
@Controller
@RequestMapping("/admin/orders")
public class OrderAdminController {
    private final OrderService orderService;
    private final RevenueBroadcaster revenueBroadcaster;


    public OrderAdminController(OrderService orderService, RevenueBroadcaster revenueBroadcaster) {
        this.orderService = orderService;
        this.revenueBroadcaster = revenueBroadcaster;
    }


    @PostMapping("/{id}/confirm")
    public String confirm(@PathVariable String id) {
        orderService.confirmOrder(id); // đổi trạng thái -> CONFIRMED/PAID, cập nhật paidAt
// đẩy realtime revenue
        BigDecimal revenueToday = orderService.sumRevenueByDate(LocalDate.now());
        revenueBroadcaster.push(revenueToday);
        return "redirect:/admin/dashboard?orderConfirmed";
    }
}
