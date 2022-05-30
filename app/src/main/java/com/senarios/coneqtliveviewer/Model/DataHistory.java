
package com.senarios.coneqtliveviewer.Model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class DataHistory {

    @SerializedName("upcoming")
    @Expose
    private List<Upcoming> upcoming = null;
    @SerializedName("past")
    @Expose
    private List<Past> past = null;

    public List<Upcoming> getUpcoming() {
        return upcoming;
    }

    public void setUpcoming(List<Upcoming> upcoming) {
        this.upcoming = upcoming;
    }

    public List<Past> getPast() {
        return past;
    }

    public void setPast(List<Past> past) {
        this.past = past;
    }

}
