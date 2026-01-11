package com.example.artflow;

public class Order {
    private String orderId;
    private String customerName;
    private String artworkTitle;
    private String orderedOn;
    private String status;
    private String artistId;

    public Order() {
        // Default constructor required for calls to DataSnapshot.getValue(Order.class)
    }
    
    public Order(String orderId, String customerName, String artworkTitle, String orderedOn, String status) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.artworkTitle = artworkTitle;
        this.orderedOn = orderedOn;
        this.status = status;
    }
    
    public Order(String orderId, String customerName, String artworkTitle, String orderedOn, String status, String artistId) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.artworkTitle = artworkTitle;
        this.orderedOn = orderedOn;
        this.status = status;
        this.artistId = artistId;
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
    
    public String getArtistId() {
        return artistId;
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
    
    public void setArtistId(String artistId) {
        this.artistId = artistId;
    }
}