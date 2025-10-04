package com.example.fruitables.order;

import lombok.*;
import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class OrderItem {

    private Long productId;
    private String title; //tên sản phẩm
    private String variant; //phân loại
    private String thumpnailUrl; //ảnh
    private Integer quantity;
    private BigDecimal unitPrice;//giá bán/chiếc
    private BigDecimal originalPrice; //giá gốc/chiếc

    public BigDecimal getLineOriginalSubtotal() {
        return originalPrice != null ? originalPrice.multiply(BigDecimal.valueOf(quantity)) : null;
    }

    public BigDecimal getLineSubtotal() {
        return unitPrice != null ? unitPrice.multiply(BigDecimal.valueOf(quantity)) : null;
    }
    public String getThumbnailUrl() {
        return thumpnailUrl;
    }
}
