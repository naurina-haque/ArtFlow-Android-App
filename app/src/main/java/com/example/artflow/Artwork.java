package com.example.artflow;

public class Artwork {
    private String title;
    private String category;
    private String description;
    private String price;
    private String categoryKey; // Used for filtering

    public Artwork(String title, String category, String description, String price, String categoryKey) {
        this.title = title;
        this.category = category;
        this.description = description;
        this.price = price;
        this.categoryKey = categoryKey;
    }

    // Getters
    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public String getPrice() {
        return price;
    }

    public String getCategoryKey() {
        return categoryKey;
    }

    // Setters
    public void setTitle(String title) {
        this.title = title;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setCategoryKey(String categoryKey) {
        this.categoryKey = categoryKey;
    }
}