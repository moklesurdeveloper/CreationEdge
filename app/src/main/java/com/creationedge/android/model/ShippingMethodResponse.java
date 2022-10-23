package com.creationedge.android.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShippingMethodResponse {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("settings")
    @Expose
    private Settings settings;

    /**
     * No args constructor for use in serialization
     *
     */

    public ShippingMethodResponse() {
    }

    public ShippingMethodResponse(Integer id, String title, Settings settings) {
        this.id = id;
        this.title = title;
        this.settings = settings;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }
}
