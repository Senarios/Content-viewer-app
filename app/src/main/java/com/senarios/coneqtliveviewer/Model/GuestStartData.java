package com.senarios.coneqtliveviewer.Model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class GuestStartData {

    @SerializedName("event")
    @Expose
    private EventGuestStart event;
    @SerializedName("user")
    @Expose
    private GuestStartUser user;

    public EventGuestStart getEvent() {
        return event;
    }

    public void setEvent(EventGuestStart event) {
        this.event = event;
    }

    public GuestStartUser getUser() {
        return user;
    }

    public void setUser(GuestStartUser user) {
        this.user = user;
    }

}
