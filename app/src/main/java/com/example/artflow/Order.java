package com.example.artflow;

public class Order {
    private String orderId;
    private String customerId;
    private String customerName; // Added to store customer's name
    private String artistId;
    private String artistName; // Added to store artist's name
    private String artworkId;
    private String artworkTitle;
    private String artworkImageUrl;
    private String status; // "Pending", "Accepted", "Rejected", "Completed"
    private String timestamp;

    public Order() {
        // Default constructor required for Firebase
    }

    public Order(String orderId, String customerId, String customerName, String artistId, String artistName, String artworkId, String artworkTitle, String artworkImageUrl, String status, String timestamp) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.customerName = customerName;
        this.artistId = artistId;
        this.artistName = artistName;
        this.artworkId = artworkId;
        this.artworkTitle = artworkTitle;
        this.artworkImageUrl = artworkImageUrl;
        this.status = status;
        this.timestamp = timestamp;
    }

    // Getters
    public String getOrderId() {
        return orderId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getArtistId() {
        return artistId;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getArtworkId() {
        return artworkId;
    }

    public String getArtworkTitle() {
        return artworkTitle;
    }

    public String getArtworkImageUrl() {
        return artworkImageUrl;
    }

    public String getStatus() {
        return status;
    }

    public String getTimestamp() {
        return timestamp;
    }

    // Setters
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setArtistId(String artistId) {
        this.artistId = artistId;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public void setArtworkId(String artworkId) {
        this.artworkId = artworkId;
    }

    public void setArtworkTitle(String artworkTitle) {
        this.artworkTitle = artworkTitle;
    }

    public void setArtworkImageUrl(String artworkImageUrl) {
        this.artworkImageUrl = artworkImageUrl;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}