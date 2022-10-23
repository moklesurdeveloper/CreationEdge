package com.creationedge.android.model;
import java.util.List;

import com.creationedge.android.data.CartResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckoutResponse {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("parent_id")
    @Expose
    private Integer parentId;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("version")
    @Expose
    private String version;
    @SerializedName("prices_include_tax")
    @Expose
    private Boolean pricesIncludeTax;
    @SerializedName("date_created")
    @Expose
    private String dateCreated;
    @SerializedName("date_modified")
    @Expose
    private String dateModified;
    @SerializedName("discount_total")
    @Expose
    private String discountTotal;
    @SerializedName("discount_tax")
    @Expose
    private String discountTax;
    @SerializedName("shipping_total")
    @Expose
    private String shippingTotal;
    @SerializedName("shipping_tax")
    @Expose
    private String shippingTax;
    @SerializedName("cart_tax")
    @Expose
    private String cartTax;
    @SerializedName("total")
    @Expose
    private String total;
    @SerializedName("total_tax")
    @Expose
    private String totalTax;
    @SerializedName("customer_id")
    @Expose
    private Integer customerId;
    @SerializedName("order_key")
    @Expose
    private String orderKey;
    @SerializedName("billing")
    @Expose
    private Billing billing;
    @SerializedName("shipping")
    @Expose
    private Shipping shipping;
    @SerializedName("payment_method")
    @Expose
    private String paymentMethod;
    @SerializedName("payment_method_title")
    @Expose
    private String paymentMethodTitle;
    @SerializedName("transaction_id")
    @Expose
    private String transactionId;
    @SerializedName("customer_ip_address")
    @Expose
    private String customerIpAddress;
    @SerializedName("customer_user_agent")
    @Expose
    private String customerUserAgent;
    @SerializedName("created_via")
    @Expose
    private String createdVia;
    @SerializedName("customer_note")
    @Expose
    private String customerNote;
    @SerializedName("cart_hash")
    @Expose
    private String cartHash;
    @SerializedName("number")
    @Expose
    private String number;
    @SerializedName("line_items")
    @Expose
    private List<CartResponse> lineItems = null;
    @SerializedName("tax_lines")
    @Expose
    private List<Object> taxLines = null;
    @SerializedName("shipping_lines")
    @Expose
    private List<ShippingLine> shippingLines = null;;

    /**
     * No args constructor for use in serialization
     */
    public CheckoutResponse() {
    }

    public CheckoutResponse(Integer id, Integer parentId, String status, String currency, String version, Boolean pricesIncludeTax, String dateCreated, String dateModified, String discountTotal, String discountTax, String shippingTotal, String shippingTax, String cartTax, String total, String totalTax, Integer customerId, String orderKey, Billing billing, Shipping shipping, String paymentMethod, String paymentMethodTitle, String transactionId, String customerIpAddress, String customerUserAgent, String createdVia, String customerNote, String cartHash, String number, List<CartResponse> lineItems, List<Object> taxLines, List<ShippingLine> shippingLines) {
        this.id = id;
        this.parentId = parentId;
        this.status = status;
        this.currency = currency;
        this.version = version;
        this.pricesIncludeTax = pricesIncludeTax;
        this.dateCreated = dateCreated;
        this.dateModified = dateModified;
        this.discountTotal = discountTotal;
        this.discountTax = discountTax;
        this.shippingTotal = shippingTotal;
        this.shippingTax = shippingTax;
        this.cartTax = cartTax;
        this.total = total;
        this.totalTax = totalTax;
        this.customerId = customerId;
        this.orderKey = orderKey;
        this.billing = billing;
        this.shipping = shipping;
        this.paymentMethod = paymentMethod;
        this.paymentMethodTitle = paymentMethodTitle;
        this.transactionId = transactionId;
        this.customerIpAddress = customerIpAddress;
        this.customerUserAgent = customerUserAgent;
        this.createdVia = createdVia;
        this.customerNote = customerNote;
        this.cartHash = cartHash;
        this.number = number;
        this.lineItems = lineItems;
        this.taxLines = taxLines;
        this.shippingLines = shippingLines;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Boolean getPricesIncludeTax() {
        return pricesIncludeTax;
    }

    public void setPricesIncludeTax(Boolean pricesIncludeTax) {
        this.pricesIncludeTax = pricesIncludeTax;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getDateModified() {
        return dateModified;
    }

    public void setDateModified(String dateModified) {
        this.dateModified = dateModified;
    }

    public String getDiscountTotal() {
        return discountTotal;
    }

    public void setDiscountTotal(String discountTotal) {
        this.discountTotal = discountTotal;
    }

    public String getDiscountTax() {
        return discountTax;
    }

    public void setDiscountTax(String discountTax) {
        this.discountTax = discountTax;
    }

    public String getShippingTotal() {
        return shippingTotal;
    }

    public void setShippingTotal(String shippingTotal) {
        this.shippingTotal = shippingTotal;
    }

    public String getShippingTax() {
        return shippingTax;
    }

    public void setShippingTax(String shippingTax) {
        this.shippingTax = shippingTax;
    }

    public String getCartTax() {
        return cartTax;
    }

    public void setCartTax(String cartTax) {
        this.cartTax = cartTax;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getTotalTax() {
        return totalTax;
    }

    public void setTotalTax(String totalTax) {
        this.totalTax = totalTax;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getOrderKey() {
        return orderKey;
    }

    public void setOrderKey(String orderKey) {
        this.orderKey = orderKey;
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

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getCustomerIpAddress() {
        return customerIpAddress;
    }

    public void setCustomerIpAddress(String customerIpAddress) {
        this.customerIpAddress = customerIpAddress;
    }

    public String getCustomerUserAgent() {
        return customerUserAgent;
    }

    public void setCustomerUserAgent(String customerUserAgent) {
        this.customerUserAgent = customerUserAgent;
    }

    public String getCreatedVia() {
        return createdVia;
    }

    public void setCreatedVia(String createdVia) {
        this.createdVia = createdVia;
    }

    public String getCustomerNote() {
        return customerNote;
    }

    public void setCustomerNote(String customerNote) {
        this.customerNote = customerNote;
    }

    public String getCartHash() {
        return cartHash;
    }

    public void setCartHash(String cartHash) {
        this.cartHash = cartHash;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public List<CartResponse> getLineItems() {
        return lineItems;
    }

    public void setLineItems(List<CartResponse> lineItems) {
        this.lineItems = lineItems;
    }

    public List<Object> getTaxLines() {
        return taxLines;
    }

    public void setTaxLines(List<Object> taxLines) {
        this.taxLines = taxLines;
    }

    public List<ShippingLine> getShippingLines() {
        return shippingLines;
    }

    public void setShippingLines(List<ShippingLine> shippingLines) {
        this.shippingLines = shippingLines;
    }
}


