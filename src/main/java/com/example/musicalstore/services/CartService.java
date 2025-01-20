package com.example.musicalstore.services;

import com.example.musicalstore.models.CartItemModel;
import com.example.musicalstore.models.CartModel;
import com.example.musicalstore.models.ProductModel;
import com.example.musicalstore.models.UserModel;
import com.example.musicalstore.repositories.CartRepository;
import com.example.musicalstore.repositories.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserService userService;

    @Autowired
    public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository, UserService userService) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.userService = userService;
    }

    public void addProductToCart(ProductModel product, int quantity) {
        try {
            UserModel user = userService.getCurrentUser(); // Get the current user
            if (user == null) {
                throw new RuntimeException("User is not authenticated");
            }

            Optional<CartModel> optionalCart = cartRepository.findByUser(user);
            CartModel cart;
            if (optionalCart.isPresent()) {
                cart = optionalCart.get();
            } else {
                cart = new CartModel();
                cart.setUser(user);
                cartRepository.save(cart);
            }

            // Check if the product already exists in the cart
            Optional<CartItemModel> existingItem = cartItemRepository.findByCartAndProduct(cart, product);
            if (existingItem.isPresent()) {
                CartItemModel item = existingItem.get();
                item.setQuantity(item.getQuantity() + quantity); // Update quantity
                cartItemRepository.save(item);
            } else {
                // Add a new item if it's not already in the cart
                CartItemModel cartItem = new CartItemModel(cart, product, quantity);
                cartItemRepository.save(cartItem);
            }

        } catch (Exception e) {
            // Log the error and rethrow it
            System.err.println("Error adding product to cart: " + e.getMessage());
            throw new RuntimeException("Error adding product to cart: " + e.getMessage());
        }
    }


    public List<CartItemModel> viewCart() {
        try {
            UserModel user = userService.getCurrentUser(); // Get the current user
            if (user == null) {
                throw new RuntimeException("User is not authenticated");
            }

            Optional<CartModel> optionalCart = cartRepository.findByUser(user);
            if (optionalCart.isEmpty()) {
                throw new RuntimeException("Cart not found for the user");
            }

            CartModel cart = optionalCart.get();
            return cartItemRepository.findAllByCart(cart); // Retrieve all items in the cart
        } catch (Exception e) {
            System.err.println("Error viewing cart: " + e.getMessage());
            throw new RuntimeException("Error viewing cart: " + e.getMessage());
        }
    }

    public void removeProductFromCart(Long cartItemId) {
        try {
            UserModel user = userService.getCurrentUser(); // Get the current user
            if (user == null) {
                throw new RuntimeException("User is not authenticated");
            }

            Optional<CartModel> optionalCart = cartRepository.findByUser(user);
            if (optionalCart.isEmpty()) {
                throw new RuntimeException("Cart not found for the user");
            }

            CartModel cart = optionalCart.get();

            // Check if the item exists in the cart
            Optional<CartItemModel> optionalCartItem = cartItemRepository.findById(cartItemId);
            if (optionalCartItem.isEmpty()) {
                throw new RuntimeException("Cart item not found");
            }

            CartItemModel cartItem = optionalCartItem.get();

            // Ensure the item belongs to the user's cart
            if (!cartItem.getCart().equals(cart)) {
                throw new RuntimeException("The item does not belong to the user's cart");
            }

            // Delete the item from the cart
            cartItemRepository.delete(cartItem);
            System.out.println("Cart item removed successfully.");
        } catch (Exception e) {
            System.err.println("Error removing item from cart: " + e.getMessage());
            throw new RuntimeException("Error removing item from cart: " + e.getMessage());
        }
    }

    public CartItemModel decreaseQuantity(Long cartItemId) throws Exception {
        CartItemModel cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new Exception("CartItem not found"));

        if (cartItem.getQuantity() <= 1) {
            throw new Exception("Quantity cannot be less than 1");
        }

        cartItem.setQuantity(cartItem.getQuantity() - 1);
        cartItem.setTotalPrice(cartItem.getQuantity() * cartItem.getProduct().getPrice());
        return cartItemRepository.save(cartItem);
    }

    public CartItemModel increaseQuantity(Long cartItemId) throws Exception {
        CartItemModel cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new Exception("CartItem not found"));

        cartItem.setQuantity(cartItem.getQuantity() + 1);
        cartItem.setTotalPrice(cartItem.getQuantity() * cartItem.getProduct().getPrice());
        return cartItemRepository.save(cartItem);
    }
}
