package com.example.musicalstore.repositories;

import com.example.musicalstore.models.CartItemModel;
import com.example.musicalstore.models.CartModel;
import com.example.musicalstore.models.ProductModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItemModel, Long> {
    Optional<CartItemModel> findByCartAndProduct(CartModel cart, ProductModel product); // Add this method
    List<CartItemModel> findAllByCart(CartModel cart);

    Optional<CartItemModel> findById(Long cartItemId);

}
