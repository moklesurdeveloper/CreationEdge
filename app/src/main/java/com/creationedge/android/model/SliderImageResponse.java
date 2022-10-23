package com.creationedge.android.model;


import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SliderImageResponse {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("guid")
    @Expose
    private SliderImage guid;
    @SerializedName("content")
    @Expose
    private Content content;
    /**
     * No args constructor for use in serialization
     */
    public SliderImageResponse() {
    }

    public SliderImageResponse(Integer id, SliderImage guid, Content content) {
        this.id = id;
        this.guid = guid;
        this.content = content;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public SliderImage getGuid() {
        return guid;
    }

    public void setGuid(SliderImage guid) {
        this.guid = guid;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }
}
