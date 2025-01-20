package com.example.musicalstore.models;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "cart")
public class CartModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartId;

    @ManyToOne
    private UserModel user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    private List<CartItemModel> items = new ArrayList<>();

    private java.time.LocalDateTime createdAt = java.time.LocalDateTime.now();

    // You can add the setter manually if Lombok isn't generating it
    public void setUser(UserModel user) {
        this.user = user;
    }

}
