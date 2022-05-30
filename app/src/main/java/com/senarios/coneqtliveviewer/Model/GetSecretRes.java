package com.senarios.coneqtliveviewer.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetSecretRes {

    @SerializedName("message")
    @Expose
    private Boolean message;
    @SerializedName("secret")
    @Expose
    private String secret;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("ephemeralKey_secret")
    @Expose
    private String ephemeralKeySecret;

    public Boolean getMessage() {
        return message;
    }

    public void setMessage(Boolean message) {
        this.message = message;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getEphemeralKeySecret() {
        return ephemeralKeySecret;
    }

    public void setEphemeralKeySecret(String ephemeralKeySecret) {
        this.ephemeralKeySecret = ephemeralKeySecret;
    }

}
