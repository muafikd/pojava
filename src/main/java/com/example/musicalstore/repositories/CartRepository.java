package com.example.musicalstore.repositories;

import com.example.musicalstore.models.CartModel;
import com.example.musicalstore.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<CartModel, Long> {
    Optional<CartModel> findByUser(UserModel user); // Add this method to find a cart by user

}
