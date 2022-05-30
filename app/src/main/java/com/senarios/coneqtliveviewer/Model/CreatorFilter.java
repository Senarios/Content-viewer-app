package com.senarios.coneqtliveviewer.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreatorFilter {
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("totalEvents")
    @Expose
    private Integer totalEvents;
    @SerializedName("cancelEvents")
    @Expose
    private Integer cancelEvents;
    @SerializedName("purchased")
    @Expose
    private Integer purchased;
    @SerializedName("refund")
    @Expose
    private double refund;

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

    public Integer getTotalEvents() {
        return totalEvents;
    }

    public void setTotalEvents(Integer totalEvents) {
        this.totalEvents = totalEvents;
    }

    public Integer getCancelEvents() {
        return cancelEvents;
    }

    public void setCancelEvents(Integer cancelEvents) {
        this.cancelEvents = cancelEvents;
    }

    public Integer getPurchased() {
        return purchased;
    }

    public void setPurchased(Integer purchased) {
        this.purchased = purchased;
    }

    public double getRefund() {
        return refund;
    }

    public void setRefund(double refund) {
        this.refund = refund;
    }

}
