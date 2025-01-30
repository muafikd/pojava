package com.example.musicalstore.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class OrderModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private UserModel user;

    private double totalPrice;
    private String orderStatus;

    @Column(nullable = false)
    private java.time.LocalDateTime placedAt = java.time.LocalDateTime.now();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<OrderItemModel> orderItems;

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public void setOrderItems(List<OrderItemModel> orderItems) {
        this.orderItems = orderItems;
    }

    public void setPlacedAt(LocalDateTime placedAt) {
        this.placedAt = placedAt;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public List<OrderItemModel> getOrderItems() {
        return orderItems;
    }

    public LocalDateTime getPlacedAt() {
        return placedAt;
    }

    public Long getOrderId() {
        return orderId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public UserModel getUser() {
        return user;
    }
}