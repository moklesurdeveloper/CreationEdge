package com.creationedge.android.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Slider {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("link")
    @Expose
    private String link;
    @SerializedName("featured_media")
    @Expose
    private Integer featuredMedia;
    /**
     * No args constructor for use in serialization
     */
    public Slider() {
    }

    public Slider(Integer id, String link, Integer featuredMedia) {
        this.id = id;
        this.link = link;
        this.featuredMedia = featuredMedia;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Integer getFeaturedMedia() {
        return featuredMedia;
    }

    public void setFeaturedMedia(Integer featuredMedia) {
        this.featuredMedia = featuredMedia;
    }
}