package com.example.fruitables.order;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public final class OrderMapper {
    private OrderMapper() {}

    public static OrderDTO toSummaryDto(Order o) {
        OrderDTO d = new OrderDTO();
        d.setId(o.getId());
        d.setCode(o.getCode());
        d.setCreatedAt(o.getCreatedAt());
        d.setStatus(o.getStatus());

        List<OrderItem> items = o.getItems();
        if (items == null || items.isEmpty()) {
            d.setTitle("(Đơn hàng)");
            d.setQuantity(0);
            d.setVariant("");
            d.setThumbnailUrl(null);
            d.setOriginalPrice(null);
            d.setTotalPrice(o.getGrandTotal());
            return d;
        }

        int totalQty = items.stream()
                .map(i -> i.getQuantity() == null ? 0 : i.getQuantity())
                .reduce(0, Integer::sum);
        d.setQuantity(totalQty);

        BigDecimal sumOriginal = items.stream()
                .map(OrderItem::getLineOriginalSubtotal)
                .filter(v -> v != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        d.setOriginalPrice(sumOriginal.compareTo(BigDecimal.ZERO) > 0 ? sumOriginal : null);

        BigDecimal sumUnit = items.stream()
                .map(OrderItem::getLineSubtotal)
                .filter(v -> v != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        d.setTotalPrice(o.getGrandTotal() != null ? o.getGrandTotal() : sumUnit);

        OrderItem first = items.get(0);
        if (items.size() == 1) {
            d.setTitle(first.getTitle());
            d.setVariant(first.getVariant());
            d.setThumbnailUrl(first.getThumbnailUrl());
        } else {
            String joinedNames = items.stream()
                    .map(OrderItem::getTitle)
                    .filter(n -> n != null && !n.isBlank())
                    .limit(2)
                    .collect(Collectors.joining(", "));
            String title = totalQty + " sản phẩm" + (joinedNames.isBlank() ? "" : " (" + joinedNames + "…)");
            d.setTitle(title);
            d.setVariant("");
            d.setThumbnailUrl(first.getThumbnailUrl());
        }
        return d;
    }
}