package com.creationedge.android.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddMeta {

    @SerializedName("test")
    @Expose
    private String test;

    /**
     * No args constructor for use in serialization
     *
     */
    public AddMeta() {
    }

    /**
     *
     * @param test
     */
    public AddMeta(String test) {
        super();
        this.test = test;
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

}