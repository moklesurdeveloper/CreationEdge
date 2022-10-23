package com.creationedge.android.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Meta {

    @SerializedName("test")
    @Expose
    private String test;
    @SerializedName("add-to-cart")
    @Expose
    private Integer addToCart;
    @SerializedName("product_id")
    @Expose
    private Integer productId;
    @SerializedName("quantity")
    @Expose
    private Integer quantity;
    @SerializedName("variation_id")
    @Expose
    private Integer variationId;

    /**
     * No args constructor for use in serialization
     *
     */
    public Meta() {
    }

    /**
     *
     * @param quantity
     * @param variationId
     * @param test
     * @param productId
     * @param addToCart
     */
    public Meta(String test, Integer addToCart, Integer productId, Integer quantity, Integer variationId) {
        super();
        this.test = test;
        this.addToCart = addToCart;
        this.productId = productId;
        this.quantity = quantity;
        this.variationId = variationId;
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    public Integer getAddToCart() {
        return addToCart;
    }

    public void setAddToCart(Integer addToCart) {
        this.addToCart = addToCart;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getVariationId() {
        return variationId;
    }

    public void setVariationId(Integer variationId) {
        this.variationId = variationId;
    }

}