package com.example.musicalstore.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JWTGenerator {

    private static final Logger logger = LoggerFactory.getLogger(JWTGenerator.class);
    private static final long EXPIRATION_TIME_MS = 10 * 60 * 1000; // 10 minutes
    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public String generateToken(Authentication authentication) {
        String email = authentication.getName(); // Uses email
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + EXPIRATION_TIME_MS);

        String roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .reduce((role1, role2) -> role1 + "," + role2) // If the user has multiple roles
                .orElse("");

        logger.info("Generating token for email: {}", email);

        return Jwts.builder()
                .setSubject(email)
                .claim("roles", roles)
                .setIssuedAt(currentDate)
                .setExpiration(expireDate)
                .signWith(key)
                .compact();
    }

    public String getEmailFromJWT(String token) {
        logger.info("Getting email from JWT token");
        Claims claims = Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public boolean validateToken(String token) {
        logger.info("Validating JWT token");
        try {
            Jwts.parser().setSigningKey(key).parseClaimsJws(token);
            logger.info("JWT token is valid");
            return true;
        } catch (Exception ex) {
            logger.error("JWT validation error: {}", ex.getMessage());
            throw new AuthenticationCredentialsNotFoundException(
                    "JWT is invalid or expired: " + ex.getMessage(), ex
            );
        }
    }
}
