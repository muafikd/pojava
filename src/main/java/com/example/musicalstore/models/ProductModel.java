package com.example.musicalstore.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
public class ProductModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @Column(columnDefinition = "TEXT")
    private String title;
    @Column(columnDefinition = "TEXT")
    private String artist;
    @Column(columnDefinition = "TEXT")
    private String genre;
    private int releaseYear;
    private double price;
    private int stockQuantity;
    @Column(columnDefinition = "TEXT")
    private String description;
    @Column(columnDefinition = "TEXT")
    private String imageUrl;
    private LocalDateTime createdAt = LocalDateTime.now();

    // Default Constructor
    public ProductModel() {
    }

    // Parameterized Constructor
    public ProductModel(Long productId, String title, String artist, String genre, int releaseYear,
                        double price, int stockQuantity, String description, String imageUrl,
                        LocalDateTime createdAt) {
        this.productId = productId;
        this.title = title;
        this.artist = artist;
        this.genre = genre;
        this.releaseYear = releaseYear;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.description = description;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
