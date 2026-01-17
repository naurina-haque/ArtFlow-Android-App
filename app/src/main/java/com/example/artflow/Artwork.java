package com.example.artflow;

public class Artwork {
    private String id;
    private String title;
    private String category;
    private String description;
    private String price;
    private String categoryKey; // Used for filtering
    private String artistId;
    private String imageUrl; // Firebase Storage download URL

    public Artwork() {
        // Default constructor required for calls to DataSnapshot.getValue(Artwork.class)
    }
    
    public Artwork(String id, String title, String category, String description, String price, String categoryKey, String artistId, String imageUrl) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.description = description;
        this.price = price;
        this.categoryKey = categoryKey;
        this.artistId = artistId;
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    // Getters
    public String getId() {
        return id;
    }

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
    
    public String getArtistId() {
        return artistId;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }
    
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
    
    public void setArtistId(String artistId) {
        this.artistId = artistId;
    }
}