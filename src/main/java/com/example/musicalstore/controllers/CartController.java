package com.example.musicalstore.controllers;

import com.example.musicalstore.dto.CartItemDTO;
import com.example.musicalstore.models.CartItemModel;
import com.example.musicalstore.services.CartService;
import com.example.musicalstore.services.ProductService;
import com.example.musicalstore.models.ProductModel;
import com.example.musicalstore.models.CartModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.musicalstore.repositories.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;
    private final ProductService productService;

    @Autowired
    public CartController(CartService cartService, ProductService productService) {
        this.cartService = cartService;
        this.productService = productService;
    }

    // Add a product to the cart
    @PostMapping("/add")
    public ResponseEntity<String> addToCart(@RequestParam Long productId, @RequestParam int quantity) {
        try {
            ProductModel product = productService.getProductById(productId); // Fetch the product by ID
            if (product != null) {
                cartService.addProductToCart(product, quantity); // Call service method to add product
                return ResponseEntity.ok("Product added to cart");
            }
            return ResponseEntity.badRequest().body("Product not found");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error adding product to cart");
        }
    }


    @GetMapping("/view")
    public ResponseEntity<?> viewCart() {
        try {
            // Fetch the cart items
            List<CartItemModel> cartItems = cartService.viewCart();

            // Map to a DTO if needed
            List<CartItemDTO> cartItemDTOs = cartItems.stream()
                    .map(item -> new CartItemDTO(
                            item.getCartItemId(),
                            item.getProduct().getTitle(),
                            item.getQuantity(),
                            item.getProduct().getPrice()))
                    .toList();

            // Return the DTOs as a response
            return ResponseEntity.ok(cartItemDTOs);
        } catch (Exception e) {
            // Log the error for debugging
            System.err.println("Error fetching cart items: " + e.getMessage());
            e.printStackTrace();

            // Return a meaningful error response
            return ResponseEntity.status(500).body("An error occurred while retrieving cart items.");
        }
    }

    @DeleteMapping("/remove/{cartItemId}")
    public ResponseEntity<String> removeFromCart(@PathVariable Long cartItemId) {
        try {
            cartService.removeProductFromCart(cartItemId);
            return ResponseEntity.ok("Cart item removed successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @PutMapping("/decrease/{cartItemId}")
    public ResponseEntity<CartItemModel> decreaseQuantity(@PathVariable Long cartItemId) {
        try {
            CartItemModel updatedCartItem = cartService.decreaseQuantity(cartItemId);
            return ResponseEntity.ok(updatedCartItem);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(null);
        }
    }

    @PutMapping("/increase/{cartItemId}")
    public ResponseEntity<CartItemModel> increaseQuantity(@PathVariable Long cartItemId) {
        try {
            CartItemModel updatedCartItem = cartService.increaseQuantity(cartItemId);
            return ResponseEntity.ok(updatedCartItem);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(null);
        }
    }


    // Other cart-related API methods (view cart, remove item, etc.)
}
