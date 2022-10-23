package com.creationedge.android.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TimmerResponse {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("end_datetime")
    @Expose
    private String endDatetime;


    /**
     * No args constructor for use in serialization
     */
    public TimmerResponse() {
    }

    public TimmerResponse(Integer id, String endDatetime) {
        this.id = id;
        this.endDatetime = endDatetime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEndDatetime() {
        return endDatetime;
    }

    public void setEndDatetime(String endDatetime) {
        this.endDatetime = endDatetime;
    }
}