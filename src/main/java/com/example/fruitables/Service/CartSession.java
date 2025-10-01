package com.example.fruitables.Service;

import com.example.fruitables.model.CartItem;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
@SessionScope
public class CartSession implements Serializable {
    private final Map<Long, CartItem> items = new LinkedHashMap<>();

    public Collection<CartItem> getItems() {
        return items.values();
    }

    public void add(CartItem item) {
        items.merge(item.getProductId(), item, (oldVal, newVal) -> {
            oldVal.setQuantity(oldVal.getQuantity() + newVal.getQuantity());
            return oldVal;
        });
    }

    public void update(Long productId, int quantity) {
        CartItem it = items.get(productId);
        if (it != null) {
            it.setQuantity(Math.max(1, quantity));
        }
    }

    public void remove(Long productId) {
        items.remove(productId);
    }

    public void clear() {
        items.clear();
    }

    public long getSubtotal() {
        return items.values().stream().mapToLong(CartItem::getSubtotal).sum();
    }

    public long getShipping() {
        return items.isEmpty() ? 0 : 3000; // ví dụ phí cố định
    }

    public long getTotal() {
        return getSubtotal() + getShipping();
    }
}
