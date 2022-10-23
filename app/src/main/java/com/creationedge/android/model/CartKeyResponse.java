package com.creationedge.android.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CartKeyResponse {
    @SerializedName("key")
    @Expose
    private String key;

    public CartKeyResponse() {
    }

    public CartKeyResponse(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
