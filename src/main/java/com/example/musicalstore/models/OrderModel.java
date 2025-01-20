package com.example.musicalstore.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "orders")
public class OrderModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @ManyToOne
    private UserModel user;

    private double totalPrice;
    private String orderStatus;

    @Column(nullable = false)
    private java.time.LocalDateTime placedAt = java.time.LocalDateTime.now();
}
