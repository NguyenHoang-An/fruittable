package com.example.fruitables.order;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderDTO {
    private String id;
    private String code;
    private LocalDateTime createdAt;
    private OrderStatus status;

    //Tóm tắt từ items
    private String title;
    private Integer quantity; //tổng số lượng
    private String variant; //của item đầu tiên
    private BigDecimal originalPrice; //tổng gốc
    private BigDecimal totalPrice; // tổng thanh toán
    private String thumbnailUrl; //ảnh
}
