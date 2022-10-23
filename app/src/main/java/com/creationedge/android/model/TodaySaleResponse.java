package com.creationedge.android.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TodaySaleResponse {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("br_products_of_day")
    @Expose
    private BrProductsOfDay brProductsOfDay;
    /**
     * No args constructor for use in serialization
     *
     */
    public TodaySaleResponse() {
    }

    public TodaySaleResponse(Integer id, BrProductsOfDay brProductsOfDay) {
        this.id = id;
        this.brProductsOfDay = brProductsOfDay;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BrProductsOfDay getBrProductsOfDay() {
        return brProductsOfDay;
    }

    public void setBrProductsOfDay(BrProductsOfDay brProductsOfDay) {
        this.brProductsOfDay = brProductsOfDay;
    }
}