package com.example.musicalstore.controllers;

import com.example.musicalstore.models.UserModel;
import com.example.musicalstore.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserModel> getUserById(@PathVariable Long id) {
        // Get the authentication information
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // Get the email of the authenticated user

        // Log the access attempt
        System.out.println("User with email " + email + " is attempting to access user ID: " + id);

        // Retrieve user details from the service
        Optional<UserModel> userOptional = userService.getUserById(id);

        // Check if the user exists
        if (userOptional.isPresent()) {
            return ResponseEntity.ok(userOptional.get());
        } else {
            return ResponseEntity.notFound().build(); // Return 404 if user not found
        }
    }

    @GetMapping("/me")
    public ResponseEntity<UserModel> getAuthenticatedUser() {
        // Get the authentication information
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // Get the email of the authenticated user

        // Retrieve user details by email
        Optional<UserModel> userOptional = userService.getUserByEmail(email);

        // Check if the user exists
        if (userOptional.isPresent()) {
            return ResponseEntity.ok(userOptional.get());
        } else {
            return ResponseEntity.notFound().build(); // Return 404 if user not found
        }
    }

    @PutMapping("/me")
    public ResponseEntity<UserModel> updateUserInfo(@RequestBody UserModel updatedUser) {
        // Get the authenticated user's email
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        // Retrieve the current user by email
        Optional<UserModel> userOptional = userService.getUserByEmail(email);
        if (userOptional.isPresent()) {
            UserModel currentUser = userOptional.get();

            // Update the fields (excluding sensitive fields if necessary)
            currentUser.setName(updatedUser.getName());
            currentUser.setEmail(updatedUser.getEmail());
            if (updatedUser.getPasswordHash() != null) {
                currentUser.setPasswordHash(updatedUser.getPasswordHash()); // Make sure to hash passwords securely
            }
            currentUser.setPhone(updatedUser.getPhone());
            currentUser.setAddress(updatedUser.getAddress());

            // Save the updated user
            UserModel savedUser = userService.saveUser(currentUser);

            return ResponseEntity.ok(savedUser);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
