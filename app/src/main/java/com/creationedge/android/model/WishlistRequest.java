package com.creationedge.android.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WishlistRequest {

    @SerializedName("product_id")
    @Expose
    private Integer productId;
    @SerializedName("variation_id")
    @Expose
    private Integer variationId;
    @SerializedName("meta")
    @Expose
    private AddMeta meta;

    /**
     * No args constructor for use in serialization
     *
     */
    public WishlistRequest() {
    }

    /**
     *
     * @param variationId
     * @param productId
     * @param meta
     */
    public WishlistRequest(Integer productId, Integer variationId, AddMeta meta) {
        super();
        this.productId = productId;
        this.variationId = variationId;
        this.meta = meta;
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

    public AddMeta getMeta() {
        return meta;
    }

    public void setMeta(AddMeta meta) {
        this.meta = meta;
    }

}