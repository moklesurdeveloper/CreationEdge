package com.creationedge.android.model;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BrProductsOfDay {

    @SerializedName("page")
    @Expose
    private String page;
    @SerializedName("products")
    @Expose
    private ArrayList<String> products = null;

    /**
     * No args constructor for use in serialization
     *
     */
    public BrProductsOfDay() {
    }

    /**
     *
     * @param page
     * @param products
     */
    public BrProductsOfDay(String page, ArrayList<String> products) {
        this.page = page;
        this.products = products;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public ArrayList<String> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<String> products) {
        this.products = products;
    }
}