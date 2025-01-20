package com.example.musicalstore.controllers;

import com.example.musicalstore.dto.LoginDto;
import com.example.musicalstore.models.LoginResponse;
import com.example.musicalstore.models.UserModel;
import com.example.musicalstore.repositories.UserRepository;
import com.example.musicalstore.security.JWTGenerator;
import com.example.musicalstore.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JWTGenerator jwtGenerator;

    @Autowired
    public AuthController(UserService userService,
                          AuthenticationManager authenticationManager,
                          UserRepository userRepository,
                          JWTGenerator jwtGenerator) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtGenerator = jwtGenerator;
    }

    // LOGIN ENDPOINT
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginDto loginDto) {
        logger.info("Login attempt with email: {}", loginDto.getEmail());

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String token = jwtGenerator.generateToken(authentication);
            logger.info("Successfully logged in with email: {}", loginDto.getEmail());

            // Set JWT as an HttpOnly cookie
            ResponseCookie jwtCookie = ResponseCookie.from("jwt", token)
                    .httpOnly(true)
                    .secure(false)  // Set to 'true' in production (for HTTPS)
                    .path("/")
                    .maxAge(24 * 60 * 60)  // 1 day expiration
                    .build();

            // Send the token in both the cookie and response body
            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                    .body(new LoginResponse(token));

        } catch (Exception e) {
            logger.error("Login failed for email: {} - {}", loginDto.getEmail(), e.getMessage());
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    // REGISTER ENDPOINT
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserModel user) {
        // Only email and passwordHash are mandatory
        if (user.getEmail() == null || user.getPasswordHash() == null) {
            return new ResponseEntity<>("Email and password are required", HttpStatus.BAD_REQUEST);
        }

        // Set other fields to null or default if not provided
        user.setName(null);
        user.setPhone(null);
        user.setAddress(null);

        userService.registerUser(user);
        logger.info("User registered with email: {}", user.getEmail());
        return new ResponseEntity<>(HttpStatus.OK);
    }



    // LOGOUT ENDPOINT
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String jwtToken = request.getHeader("Authorization");
        if (jwtToken != null && jwtToken.startsWith("Bearer ")) {
            jwtToken = jwtToken.substring(7);
            logger.info("Logging out user with token: {}", jwtToken);
        }

        // Clear JWT by sending an expired cookie
        ResponseCookie expiredCookie = ResponseCookie.from("jwt", "")
                .httpOnly(true)
                .secure(false)  // Set to 'true' in production (for HTTPS)
                .path("/")
                .maxAge(0)  // Expire immediately
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, expiredCookie.toString())
                .body("Successfully logged out");
    }
}
