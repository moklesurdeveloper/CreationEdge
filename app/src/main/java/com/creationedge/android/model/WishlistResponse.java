package com.creationedge.android.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WishlistResponse {

    @SerializedName("item_id")
    @Expose
    private Integer itemId;
    @SerializedName("product_id")
    @Expose
    private Integer productId;
    @SerializedName("variation_id")
    @Expose
    private Integer variationId;
    @SerializedName("meta")
    @Expose
    private Meta meta;
    @SerializedName("date_added")
    @Expose
    private String dateAdded;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("in_stock")
    @Expose
    private Boolean inStock;

    /**
     * No args constructor for use in serialization
     *
     */
    public WishlistResponse() {
    }

    /**
     *
     * @param itemId
     * @param variationId
     * @param productId
     * @param meta
     * @param price
     * @param inStock
     * @param dateAdded
     */
    public WishlistResponse(Integer itemId, Integer productId, Integer variationId, Meta meta, String dateAdded, String price, Boolean inStock) {
        super();
        this.itemId = itemId;
        this.productId = productId;
        this.variationId = variationId;
        this.meta = meta;
        this.dateAdded = dateAdded;
        this.price = price;
        this.inStock = inStock;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getVariationId() {
        return variationId;
    }

    public void setVariationId(Integer variationId) {
        this.variationId = variationId;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Boolean getInStock() {
        return inStock;
    }

    public void setInStock(Boolean inStock) {
        this.inStock = inStock;
    }

}