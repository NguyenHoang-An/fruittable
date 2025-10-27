package com.example.fruitables.admin;


import com.example.fruitables.order.Order;
import com.example.fruitables.order.OrderService;
import com.example.fruitables.product.Product;
import com.example.fruitables.user.User;
import com.example.fruitables.user.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


@PreAuthorize("hasRole('ADMIN')")
@Controller
@RequestMapping("/admin")
public class AdminDashboardController {
    private final OrderService orderService;
    private final UserRepository userRepository;
    private final RevenueBroadcaster broadcaster;


    public AdminDashboardController(OrderService orderService,
                                    UserRepository userRepository,
                                    RevenueBroadcaster broadcaster) {
        this.orderService = orderService;
        this.userRepository = userRepository;
        this.broadcaster = broadcaster;
    }


    @GetMapping("/dashboard")
    public String dashboard(Model model) {
// KPI
        BigDecimal revenueToday = orderService.sumRevenueByDate(LocalDate.now());
        long ordersPending = orderService.countPending();
        long customers = userRepository.count();


        List<Order> latestOrders = orderService.findLatest(10);
        List<User> latestCustomers = userRepository.findTop10ByOrderByCreatedAtDesc();


        model.addAttribute("revenueToday", revenueToday);
        model.addAttribute("ordersPending", ordersPending);
        model.addAttribute("customers", customers);
        model.addAttribute("latestOrders", latestOrders);
        model.addAttribute("latestCustomers", latestCustomers);
        return "admin/dashboard";
    }

    @GetMapping("/products/new")
    public String productForm(Model model) {
        model.addAttribute("page", "products");
        model.addAttribute("pageTitle", "Thêm sản phẩm");
        model.addAttribute("product", new Product());
        return "admin/product-form";
    }


    // SSE stream cho biểu đồ realtime
    @GetMapping("/api/revenue-stream")
    public SseEmitter revenueStream() {
        return broadcaster.subscribe();
    }
}