package com.creationedge.android.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Content {

    @SerializedName("rendered")
    @Expose
    private String rendered;
    @SerializedName("protected")
    @Expose
    private Boolean _protected;

    /**
     * No args constructor for use in serialization
     *
     */
    public Content() {
    }

    /**
     *
     * @param rendered
     * @param _protected
     */
    public Content(String rendered, Boolean _protected) {
        super();
        this.rendered = rendered;
        this._protected = _protected;
    }

    public String getRendered() {
        return rendered;
    }

    public void setRendered(String rendered) {
        this.rendered = rendered;
    }

    public Boolean getProtected() {
        return _protected;
    }

    public void setProtected(Boolean _protected) {
        this._protected = _protected;
    }

}