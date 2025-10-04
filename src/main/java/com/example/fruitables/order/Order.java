package com.example.fruitables.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(collection = "orders")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@CompoundIndexes({
    @CompoundIndex(name = "uid_status_idx", def = "{'userId':1, 'status':1}"),
    @CompoundIndex(name = "code_unique_idx", def = "{'code':1}", unique = true)
})
public class Order {
    @Id
    private String id;             // Mongo ObjectId dưới dạng String

    @Indexed
    private String userId;         // id user (String cho khớp Mongo)

    private String code;           // mã đơn (unique index ở trên)
    private LocalDateTime createdAt;

    private OrderStatus status;
    private BigDecimal grandTotal; // tổng thanh toán (tự tính sẵn nếu muốn)

    // Embed toàn bộ items trong 1 tài liệu Order
    @Builder.Default
    private List<OrderItem> items = new ArrayList<>();
}