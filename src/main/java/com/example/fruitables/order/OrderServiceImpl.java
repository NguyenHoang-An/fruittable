package com.example.fruitables.order;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
