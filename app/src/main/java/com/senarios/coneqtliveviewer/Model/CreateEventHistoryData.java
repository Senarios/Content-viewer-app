
package com.senarios.coneqtliveviewer.Model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class CreateEventHistoryData {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private DataHistory data;

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

    public DataHistory getData() {
        return data;
    }

    public void setData(DataHistory data) {
        this.data = data;
    }

}
