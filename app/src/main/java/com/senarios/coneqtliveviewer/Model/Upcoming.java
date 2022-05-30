
package com.senarios.coneqtliveviewer.Model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Upcoming {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("content_creator_id")
    @Expose
    private Integer contentCreatorId;
    @SerializedName("event_id")
    @Expose
    private Integer eventId;
    @SerializedName("content_viewer_id")
    @Expose
    private Integer contentViewerId;
    @SerializedName("ticket_price")
    @Expose
    private double ticketPrice;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("otp")
    @Expose
    private Integer otp;
    @SerializedName("stripe_payment_id")
    @Expose
    private String stripePaymentId;
    @SerializedName("status_entry")
    @Expose
    private Integer statusEntry;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("events")
    @Expose
    private EventsHistory events;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getContentCreatorId() {
        return contentCreatorId;
    }

    public void setContentCreatorId(Integer contentCreatorId) {
        this.contentCreatorId = contentCreatorId;
    }

    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    public Integer getContentViewerId() {
        return contentViewerId;
    }

    public void setContentViewerId(Integer contentViewerId) {
        this.contentViewerId = contentViewerId;
    }

    public double getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(double ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getOtp() {
        return otp;
    }

    public void setOtp(Integer otp) {
        this.otp = otp;
    }

    public String getStripePaymentId() {
        return stripePaymentId;
    }

    public void setStripePaymentId(String stripePaymentId) {
        this.stripePaymentId = stripePaymentId;
    }

    public Integer getStatusEntry() {
        return statusEntry;
    }

    public void setStatusEntry(Integer statusEntry) {
        this.statusEntry = statusEntry;
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

    public EventsHistory getEvents() {
        return events;
    }

    public void setEvents(EventsHistory events) {
        this.events = events;
    }

}
