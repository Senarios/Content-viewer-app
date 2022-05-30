package com.senarios.coneqtliveviewer.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataGuestVerify {

    @SerializedName("content_viewer_id")
    @Expose
    private Integer contentViewerId;
    @SerializedName("event_id")
    @Expose
    private Integer eventId;

    public Integer getContentViewerId() {
        return contentViewerId;
    }

    public void setContentViewerId(Integer contentViewerId) {
        this.contentViewerId = contentViewerId;
    }

    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

}
