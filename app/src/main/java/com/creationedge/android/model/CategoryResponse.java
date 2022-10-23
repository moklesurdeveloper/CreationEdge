package com.creationedge.android.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CategoryResponse {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("image")
    @Expose
    private ProductImageResponse image;
    /**
     * No args constructor for use in serialization
     *
     */
    public CategoryResponse() {
    }


    public CategoryResponse(Integer id, String name, ProductImageResponse image) {
        this.id = id;
        this.name = name;
        this.image = image;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProductImageResponse getImage() {
        return image;
    }

    public void setImage(ProductImageResponse image) {
        this.image = image;
    }
}
