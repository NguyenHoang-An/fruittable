package com.example.fruitables.order;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    @Override
    public List<OrderDTO> findOrdersOf(String userId, List<OrderStatus> statuses) {
        return orderRepository.findByUserIdAndStatusIn(userId, statuses)
                .stream().map(OrderMapper::toSummaryDto).toList();
    }

    @Override
    public List<OrderDTO> findOrdersOf(String userId, OrderStatus status) {
        return orderRepository.findByUserIdAndStatus(userId, status)
                .stream().map(OrderMapper::toSummaryDto).toList();
    }

    @Override
    public List<OrderDTO> findOrdersOf(String userId, List<OrderStatus> statuses,
                                       LocalDateTime from, LocalDateTime to) {
        return orderRepository.findByUserAndStatusesInDateRange(userId, statuses, from, to)
                .stream().map(OrderMapper::toSummaryDto).toList();
    }

    @Override
    public void confirmOrder(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(OrderStatus.CONFIRMED);
        orderRepository.save(order);
    }

    @Override
    public BigDecimal sumRevenueByDate(LocalDate date){
        List<Order> orders = orderRepository.findAll();
        BigDecimal total = BigDecimal.ZERO;

        for (Order order : orders){
            if (order.getCreatedAt().toLocalDate().equals(date)
            && order.getStatus() == OrderStatus.CONFIRMED){
                total = total.add(order.getGrandTotal());
            }
        }
        return total;
    }

    @Override
    public long countPending() {
        return orderRepository.countByStatus(OrderStatus.PENDING);
    }

    @Override
    public List<Order> findLatest(int limit) {
        // MongoRepository không hỗ trợ limit động, nên ta xử lý bằng Pageable
        Pageable pageable = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
        return orderRepository.findAll(pageable).getContent();
    }

}
