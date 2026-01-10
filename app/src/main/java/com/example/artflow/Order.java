package com.example.artflow;

public class Order {
    private String orderId;
    private String customerName;
    private String artworkTitle;
    private String orderedOn;
    private String status;

    public Order(String orderId, String customerName, String artworkTitle, String orderedOn, String status) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.artworkTitle = artworkTitle;
        this.orderedOn = orderedOn;
        this.status = status;
    }

    // Getters
    public String getOrderId() {
        return orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getArtworkTitle() {
        return artworkTitle;
    }

    public String getOrderedOn() {
        return orderedOn;
    }

    public String getStatus() {
        return status;
    }

    // Setters
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setArtworkTitle(String artworkTitle) {
        this.artworkTitle = artworkTitle;
    }

    public void setOrderedOn(String orderedOn) {
        this.orderedOn = orderedOn;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}