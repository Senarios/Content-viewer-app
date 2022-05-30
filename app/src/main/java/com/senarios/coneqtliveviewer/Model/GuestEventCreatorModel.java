package com.senarios.coneqtliveviewer.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GuestEventCreatorModel {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("auth_type")
    @Expose
    private Object authType;
    @SerializedName("social_id")
    @Expose
    private Object socialId;
    @SerializedName("email_verified_at")
    @Expose
    private Object emailVerifiedAt;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("verification_code")
    @Expose
    private Object verificationCode;
    @SerializedName("image_url")
    @Expose
    private String imageUrl;
    @SerializedName("image_url1")
    @Expose
    private String imageUrl1;
    @SerializedName("stripe_account_id")
    @Expose
    private String stripeAccountId;
    @SerializedName("stripe_payout")
    @Expose
    private Integer stripe_payout;
    @SerializedName("api_token")
    @Expose
    private String apiToken;
    @SerializedName("social_image_url")
    @Expose
    private Object socialImageUrl;
    @SerializedName("device_token")
    @Expose
    private String deviceToken;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    @SerializedName("stripe_customer_id")
    @Expose
    private String stripe_customer_id;
    @SerializedName("totalCreatedEvents")
    @Expose
    private Integer totalCreatedEvents;
    @SerializedName("active")
    @Expose
    private Integer active;
    @SerializedName("device_notification")
    @Expose
    private Integer device_notification;
    @SerializedName("device_token_web")
    @Expose
    private String device_token_web;

    public String getStripe_customer_id() {
        return stripe_customer_id;
    }

    public void setStripe_customer_id(String stripe_customer_id) {
        this.stripe_customer_id = stripe_customer_id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Object getAuthType() {
        return authType;
    }

    public void setAuthType(Object authType) {
        this.authType = authType;
    }

    public Object getSocialId() {
        return socialId;
    }

    public void setSocialId(Object socialId) {
        this.socialId = socialId;
    }

    public Object getEmailVerifiedAt() {
        return emailVerifiedAt;
    }

    public void setEmailVerifiedAt(Object emailVerifiedAt) {
        this.emailVerifiedAt = emailVerifiedAt;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Object getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(Object verificationCode) {
        this.verificationCode = verificationCode;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl1() {
        return imageUrl1;
    }

    public void setImageUrl1(String imageUrl1) {
        this.imageUrl1 = imageUrl1;
    }

    public String getStripeAccountId() {
        return stripeAccountId;
    }

    public void setStripeAccountId(String stripeAccountId) {
        this.stripeAccountId = stripeAccountId;
    }

    public String getApiToken() {
        return apiToken;
    }

    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
    }

    public Object getSocialImageUrl() {
        return socialImageUrl;
    }

    public void setSocialImageUrl(Object socialImageUrl) {
        this.socialImageUrl = socialImageUrl;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getStripe_payout() {
        return stripe_payout;
    }

    public void setStripe_payout(Integer stripe_payout) {
        this.stripe_payout = stripe_payout;
    }

    public Integer getTotalCreatedEvents() {
        return totalCreatedEvents;
    }

    public void setTotalCreatedEvents(Integer totalCreatedEvents) {
        this.totalCreatedEvents = totalCreatedEvents;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    public Integer getDevice_notification() {
        return device_notification;
    }

    public void setDevice_notification(Integer device_notification) {
        this.device_notification = device_notification;
    }

    public String getDevice_token_web() {
        return device_token_web;
    }

    public void setDevice_token_web(String device_token_web) {
        this.device_token_web = device_token_web;
    }
}
