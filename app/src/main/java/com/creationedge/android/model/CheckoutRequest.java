package com.creationedge.android.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

public class CheckoutRequest {
    @SerializedName("payment_method")
    @Expose
    private String paymentMethod;
    @SerializedName("payment_method_title")
    @Expose
    private String paymentMethodTitle;
    @SerializedName("transaction_id")
    @Expose
    private String transaction_id;
    @SerializedName("number")
    @Expose
    private String number;
    @SerializedName("customer_note")
    @Expose
    private String customer_note;
    @SerializedName("set_paid")
    @Expose
    private Boolean setPaid;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("customer_id")
    @Expose
    private String customer_id;
    @SerializedName("billing")
    @Expose
    private Billing billing;
    @SerializedName("shipping")
    @Expose
    private Shipping shipping;
    @SerializedName("line_items")
    @Expose
    private List<CartRequest> lineItems = null;
    @SerializedName("shipping_lines")
    @Expose
    private List<ShippingLine> shippingLines = null;

    /**
     * No args constructor for use in serialization
     */
    public CheckoutRequest() {
    }

    public CheckoutRequest(String paymentMethod, String paymentMethodTitle, String transaction_id, String number, String customer_note, Boolean setPaid, String status, String customer_id, Billing billing, Shipping shipping, List<CartRequest> lineItems, List<ShippingLine> shippingLines) {
        this.paymentMethod = paymentMethod;
        this.paymentMethodTitle = paymentMethodTitle;
        this.transaction_id = transaction_id;
        this.number = number;
        this.customer_note = customer_note;
        this.setPaid = setPaid;
        this.status = status;
        this.customer_id = customer_id;
        this.billing = billing;
        this.shipping = shipping;
        this.lineItems = lineItems;
        this.shippingLines = shippingLines;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentMethodTitle() {
        return paymentMethodTitle;
    }

    public void setPaymentMethodTitle(String paymentMethodTitle) {
        this.paymentMethodTitle = paymentMethodTitle;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCustomer_note() {
        return customer_note;
    }

    public void setCustomer_note(String customer_note) {
        this.customer_note = customer_note;
    }

    public Boolean getSetPaid() {
        return setPaid;
    }

    public void setSetPaid(Boolean setPaid) {
        this.setPaid = setPaid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public Billing getBilling() {
        return billing;
    }

    public void setBilling(Billing billing) {
        this.billing = billing;
    }

    public Shipping getShipping() {
        return shipping;
    }

    public void setShipping(Shipping shipping) {
        this.shipping = shipping;
    }

    public List<CartRequest> getLineItems() {
        return lineItems;
    }

    public void setLineItems(List<CartRequest> lineItems) {
        this.lineItems = lineItems;
    }

    public List<ShippingLine> getShippingLines() {
        return shippingLines;
    }

    public void setShippingLines(List<ShippingLine> shippingLines) {
        this.shippingLines = shippingLines;
    }
}