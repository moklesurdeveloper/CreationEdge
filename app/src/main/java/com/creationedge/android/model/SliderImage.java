package com.creationedge.android.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SliderImage {
    @SerializedName("rendered")
    @Expose
    private String rendered;

    /**
     * No args constructor for use in serialization
     *
     */
    public SliderImage() {
    }

    /**
     *
     * @param rendered
     */
    public SliderImage(String rendered) {
        super();
        this.rendered = rendered;
    }

    public String getRendered() {
        return rendered;
    }

    public void setRendered(String rendered) {
        this.rendered = rendered;
    }

}