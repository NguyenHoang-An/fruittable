package com.example.fruitables.order;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderService {
    // Lấy nhiều trạng thái
    List<OrderDTO> findOrdersOf(String userId, List<OrderStatus> statuses);

    // Lấy 1 trạng thái
    List<OrderDTO> findOrdersOf(String userId, OrderStatus status);

    // Lọc theo khoảng thời gian
    List<OrderDTO> findOrdersOf(String userId, List<OrderStatus> statuses,
                                LocalDateTime from, LocalDateTime to);
}
