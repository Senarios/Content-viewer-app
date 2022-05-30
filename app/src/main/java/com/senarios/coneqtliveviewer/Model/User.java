package com.senarios.coneqtliveviewer.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class User {

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
    private Object imageUrl;
    @SerializedName("image_url1")
    @Expose
    private Object imageUrl1;
    @SerializedName("stripe_account_id")
    @Expose
    private Object stripeAccountId;
    @SerializedName("stripe_payout")
    @Expose
    private Integer stripePayout;
    @SerializedName("api_token")
    @Expose
    private String apiToken;
    @SerializedName("social_image_url")
    @Expose
    private Object socialImageUrl;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

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

    public Object getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(Object imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Object getImageUrl1() {
        return imageUrl1;
    }

    public void setImageUrl1(Object imageUrl1) {
        this.imageUrl1 = imageUrl1;
    }

    public Object getStripeAccountId() {
        return stripeAccountId;
    }

    public void setStripeAccountId(Object stripeAccountId) {
        this.stripeAccountId = stripeAccountId;
    }

    public Integer getStripePayout() {
        return stripePayout;
    }

    public void setStripePayout(Integer stripePayout) {
        this.stripePayout = stripePayout;
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

}
