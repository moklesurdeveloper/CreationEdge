package com.creationedge.android.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShareKeyResponse {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("date_added")
    @Expose
    private String dateAdded;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("share_key")
    @Expose
    private String shareKey;

    /**
     * No args constructor for use in serialization
     *
     */
    public ShareKeyResponse() {
    }

    /**
     *
     * @param shareKey
     * @param id
     * @param title
     * @param userId
     * @param dateAdded
     */
    public ShareKeyResponse(Integer id, Integer userId, String dateAdded, String title, String shareKey) {
        super();
        this.id = id;
        this.userId = userId;
        this.dateAdded = dateAdded;
        this.title = title;
        this.shareKey = shareKey;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShareKey() {
        return shareKey;
    }

    public void setShareKey(String shareKey) {
        this.shareKey = shareKey;
    }

}