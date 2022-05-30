package com.senarios.coneqtliveviewer.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GuestEventResponse {
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("creator")
    @Expose
    private GuestEventCreatorModel creatorData;
    @SerializedName("event")
    @Expose
    private GuestEventModel eventData;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public GuestEventCreatorModel getCreatorData() {
        return creatorData;
    }

    public void setCreatorData(GuestEventCreatorModel creatorData) {
        this.creatorData = creatorData;
    }

    public GuestEventModel getEventData() {
        return eventData;
    }

    public void setEventData(GuestEventModel eventData) {
        this.eventData = eventData;
    }
}
