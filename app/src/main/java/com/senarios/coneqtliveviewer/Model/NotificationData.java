package com.senarios.coneqtliveviewer.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class NotificationData {

    @SerializedName("today")
    @Expose
    private List<Today> today = null;
    @SerializedName("earlier")
    @Expose
    private List<Earlier> earlier = null;

    public List<Today> getToday() {
        return today;
    }

    public void setToday(List<Today> today) {
        this.today = today;
    }

    public List<Earlier> getEarlier() {
        return earlier;
    }

    public void setEarlier(List<Earlier> earlier) {
        this.earlier = earlier;
    }

}
