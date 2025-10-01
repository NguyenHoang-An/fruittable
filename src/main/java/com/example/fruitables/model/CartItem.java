package com.example.fruitables.model;

import java.io.Serializable;

public class CartItem implements Serializable {
    private Long productId;
    private String name;
    private String image;
    private long unitPrice;
    private int quantity;

    public long getSubtotal(){
        return unitPrice * quantity;
    }

    //getters. setters

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public long getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(long unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
