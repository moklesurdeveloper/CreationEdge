package com.creationedge.android.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductImageResponse {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("src")
    @Expose
    private String src;

    public ProductImageResponse() {
    }

    public ProductImageResponse(Integer id, String src) {
        this.id = id;
        this.src = src;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }
}
