package com.example.musicalstore.dto;

public class CartItemDTO {
    private Long cartItemId;
    private String productName;
    private int quantity;
    private double productPrice;  // Add product price
    private double totalPrice;    // Add total price

    public CartItemDTO(Long cartItemId, String productName, int quantity, double productPrice) {
        this.cartItemId = cartItemId;
        this.productName = productName;
        this.quantity = quantity;
        this.productPrice = productPrice;
        this.totalPrice = productPrice * quantity; // Calculate total price
    }

    // Getters and setters
    public Long getCartItemId() {
        return cartItemId;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public double getTotalPrice() {
        return totalPrice;
    }
}
