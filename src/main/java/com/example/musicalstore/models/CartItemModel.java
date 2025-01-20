package com.example.musicalstore.models;
import jakarta.persistence.*;
import lombok.*;
import com.example.musicalstore.repositories.CartItemRepository;

@Entity
@Table(name = "cart_items")
public class CartItemModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartItemId;

    @ManyToOne
    private CartModel cart;

    @ManyToOne
    private ProductModel product;

    private int quantity;

    private double totalPrice; // Add this field

    public CartItemModel() {
    }

    public CartItemModel(CartModel cart, ProductModel product, int quantity) {
        this.cart = cart;
        this.product = product;
        this.quantity = quantity;
        this.totalPrice = quantity * product.getPrice(); // Initialize total price
    }

    public Long getCartItemId() {
        return cartItemId;
    }

    public CartModel getCart() {
        return cart;
    }

    public ProductModel getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
