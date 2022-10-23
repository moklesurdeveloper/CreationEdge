package com.creationedge.android.model;

public class CartRequest {
    String product_id;
    String quantity;
    String cart_item_key;

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getCart_item_key() {
        return cart_item_key;
    }

    public void setCart_item_key(String cart_item_key) {
        this.cart_item_key = cart_item_key;
    }
}
