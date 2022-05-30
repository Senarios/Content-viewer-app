package com.senarios.coneqtliveviewer.Model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class JoinGuestModel {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private JoinData data;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public JoinData getData() {
        return data;
    }

    public void setData(JoinData data) {
        this.data = data;
    }

}
