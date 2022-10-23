package com.creationedge.android.model;


import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CartData {

    @SerializedName("key")
    @Expose
    private String key;
    @SerializedName("product_id")
    @Expose
    private int productId;
    @SerializedName("variation_id")
    @Expose
    private int variationId;
    @SerializedName("quantity")
    @Expose
    private int quantity;
    @SerializedName("data_hash")
    @Expose
    private String dataHash;
    @SerializedName("line_subtotal")
    @Expose
    private int lineSubtotal;
    @SerializedName("line_subtotal_tax")
    @Expose
    private int lineSubtotalTax;
    @SerializedName("line_total")
    @Expose
    private int lineTotal;
    @SerializedName("line_tax")
    @Expose
    private int lineTax;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("product_title")
    @Expose
    private String productTitle;
    @SerializedName("product_price")
    @Expose
    private String productPrice;
    @SerializedName("product_image")
    @Expose
    private String product_image;

    /**
     * No args constructor for use in serialization
     *
     */
    public CartData() {
    }



    public CartData(String key, int productId, int variationId, int quantity, String dataHash, int lineSubtotal, int lineSubtotalTax, int lineTotal, int lineTax, String productName, String productTitle, String productPrice, String product_image) {
        this.key = key;
        this.productId = productId;
        this.variationId = variationId;
        this.quantity = quantity;
        this.dataHash = dataHash;
        this.lineSubtotal = lineSubtotal;
        this.lineSubtotalTax = lineSubtotalTax;
        this.lineTotal = lineTotal;
        this.lineTax = lineTax;
        this.productName = productName;
        this.productTitle = productTitle;
        this.productPrice = productPrice;
        this.product_image = product_image;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getVariationId() {
        return variationId;
    }

    public void setVariationId(int variationId) {
        this.variationId = variationId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDataHash() {
        return dataHash;
    }

    public void setDataHash(String dataHash) {
        this.dataHash = dataHash;
    }

    public int getLineSubtotal() {
        return lineSubtotal;
    }

    public void setLineSubtotal(int lineSubtotal) {
        this.lineSubtotal = lineSubtotal;
    }

    public int getLineSubtotalTax() {
        return lineSubtotalTax;
    }

    public void setLineSubtotalTax(int lineSubtotalTax) {
        this.lineSubtotalTax = lineSubtotalTax;
    }

    public int getLineTotal() {
        return lineTotal;
    }

    public void setLineTotal(int lineTotal) {
        this.lineTotal = lineTotal;
    }

    public int getLineTax() {
        return lineTax;
    }

    public void setLineTax(int lineTax) {
        this.lineTax = lineTax;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }
}