package com.example.fruitables.order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface OrderRepository extends MongoRepository<Order, String> {

    // Tìm theo user + nhiều trạng thái
    List<Order> findByUserIdAndStatusIn(String userId, Collection<OrderStatus> statuses);

    // Tìm theo user + 1 trạng thái
    List<Order> findByUserIdAndStatus(String userId, OrderStatus status);

    // (tuỳ chọn) Query với sort hoặc date range nếu cần
    @Query(value = "{ 'userId': ?0, 'status': { $in: ?1 }, 'createdAt': { $gte: ?2, $lte: ?3 } }")
    List<Order> findByUserAndStatusesInDateRange(String userId,
                                                 Collection<OrderStatus> statuses,
                                                 LocalDateTime from,
                                                 LocalDateTime to);
    long countByStatus(OrderStatus status);

}