package com.creationedge.android.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShippingMethodCost {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("label")
    @Expose
    private String label;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("default")
    @Expose
    private String _default;
    @SerializedName("tip")
    @Expose
    private String tip;
    @SerializedName("placeholder")
    @Expose
    private String placeholder;

    /**
     * No args constructor for use in serialization
     *
     */
    public ShippingMethodCost() {
    }

    /**
     *
     * @param _default
     * @param description
     * @param tip
     * @param id
     * @param label
     * @param placeholder
     * @param type
     * @param value
     */
    public ShippingMethodCost(String id, String label, String description, String type, String value, String _default, String tip, String placeholder) {
        super();
        this.id = id;
        this.label = label;
        this.description = description;
        this.type = type;
        this.value = value;
        this._default = _default;
        this.tip = tip;
        this.placeholder = placeholder;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDefault() {
        return _default;
    }

    public void setDefault(String _default) {
        this._default = _default;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

}