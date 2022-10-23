package com.creationedge.android.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Settings {

    @SerializedName("title")
    @Expose
    private ShippingMethodTitle title;
    @SerializedName("cost")
    @Expose
    private ShippingMethodCost cost;

    /**
     * No args constructor for use in serialization
     *
     */
    public Settings() {
    }

    /**
     *
     * @param cost
     * @param title
     */
    public Settings(ShippingMethodTitle title,ShippingMethodCost cost) {
        super();
        this.title = title;
        this.cost = cost;
    }

    public ShippingMethodTitle getTitle() {
        return title;
    }

    public void setTitle(ShippingMethodTitle title) {
        this.title = title;
    }

    public ShippingMethodCost getCost() {
        return cost;
    }

    public void setCost(ShippingMethodCost cost) {
        this.cost = cost;
    }

}