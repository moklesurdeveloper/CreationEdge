package com.creationedge.android.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CartDBModel {

    private int _id;

    private int productId;

    private int quantity;

    private int lineTotal;
    private int singlePrice;
    private String productName;

    private String product_image;

    public CartDBModel() {
    }


    public CartDBModel(int _id, int productId, int quantity, int lineTotal, int singlePrice, String productName, String product_image) {
        this._id = _id;
        this.productId = productId;
        this.quantity = quantity;
        this.lineTotal = lineTotal;
        this.singlePrice = singlePrice;
        this.productName = productName;
        this.product_image = product_image;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getLineTotal() {
        return lineTotal;
    }

    public void setLineTotal(int lineTotal) {
        this.lineTotal = lineTotal;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public int getSinglePrice() {
        return singlePrice;
    }

    public void setSinglePrice(int singlePrice) {
        this.singlePrice = singlePrice;
    }
}
