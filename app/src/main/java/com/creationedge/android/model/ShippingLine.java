package com.creationedge.android.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShippingLine {

    @SerializedName("method_id")
    @Expose
    private String methodId;
    @SerializedName("method_title")
    @Expose
    private String methodTitle;
    @SerializedName("total")
    @Expose
    private String total;

    /**
     * No args constructor for use in serialization
     *
     */
    public ShippingLine() {
    }

    /**
     *
     * @param total
     * @param methodTitle
     * @param methodId
     */
    public ShippingLine(String methodId, String methodTitle, String total) {
        super();
        this.methodId = methodId;
        this.methodTitle = methodTitle;
        this.total = total;
    }

    public String getMethodId() {
        return methodId;
    }

    public void setMethodId(String methodId) {
        this.methodId = methodId;
    }

    public String getMethodTitle() {
        return methodTitle;
    }

    public void setMethodTitle(String methodTitle) {
        this.methodTitle = methodTitle;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

}