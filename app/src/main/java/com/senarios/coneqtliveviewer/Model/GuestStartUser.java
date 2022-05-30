package com.senarios.coneqtliveviewer.Model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class GuestStartUser {

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
    private Object imageUrl1;
    @SerializedName("social_image_url")
    @Expose
    private Object socialImageUrl;
    @SerializedName("stripe_account_id")
    @Expose
    private String stripeAccountId;
    @SerializedName("stripe_customer_id")
    @Expose
    private String stripeCustomerId;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("api_token")
    @Expose
    private Object apiToken;
    @SerializedName("device_token")
    @Expose
    private Object deviceToken;
    @SerializedName("device_token_web")
    @Expose
    private Object deviceTokenWeb;
    @SerializedName("active")
    @Expose
    private Integer active;
    @SerializedName("created_at")
    @Expose
    private Object createdAt;
    @SerializedName("updated_at")
    @Expose
    private Object updatedAt;

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

    public Object getImageUrl1() {
        return imageUrl1;
    }

    public void setImageUrl1(Object imageUrl1) {
        this.imageUrl1 = imageUrl1;
    }

    public Object getSocialImageUrl() {
        return socialImageUrl;
    }

    public void setSocialImageUrl(Object socialImageUrl) {
        this.socialImageUrl = socialImageUrl;
    }

    public String getStripeAccountId() {
        return stripeAccountId;
    }

    public void setStripeAccountId(String stripeAccountId) {
        this.stripeAccountId = stripeAccountId;
    }

    public String getStripeCustomerId() {
        return stripeCustomerId;
    }

    public void setStripeCustomerId(String stripeCustomerId) {
        this.stripeCustomerId = stripeCustomerId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getApiToken() {
        return apiToken;
    }

    public void setApiToken(Object apiToken) {
        this.apiToken = apiToken;
    }

    public Object getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(Object deviceToken) {
        this.deviceToken = deviceToken;
    }

    public Object getDeviceTokenWeb() {
        return deviceTokenWeb;
    }

    public void setDeviceTokenWeb(Object deviceTokenWeb) {
        this.deviceTokenWeb = deviceTokenWeb;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    public Object getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Object createdAt) {
        this.createdAt = createdAt;
    }

    public Object getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Object updatedAt) {
        this.updatedAt = updatedAt;
    }

}
