package com.example.AuthenticationService.Security.jwt;

import com.example.AuthenticationService.Security.services.UserDetailsImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Getter
@Component
public class JwtUtils {

    @Value("${jwt.secretKey}") // Secret key for signing the JWT
    private String secretKey;

    @Value("${jwt.expiration}") // Expiration time for the token (e.g., 3600000ms = 1 hour)
    private Long expiration;

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    /**
     * Generate a JWT token for the authenticated user.
     * @param authentication the authentication object containing user details.
     * @return the generated JWT.
     */
    public String generateToken(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        // Create the JWT with the username, issue date, and expiration date
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("email", userDetails.getEmail()) // Add email as a custom claim
                .claim("roles", userDetails.getAuthorities())// Subject is the username of the user
                .setIssuedAt(new Date()) // Token creation time
                .setExpiration(new Date(new Date().getTime() + expiration)) // Expiration time
                .signWith(key(), SignatureAlgorithm.HS256) // Signing the token with secret key
                .compact(); // Generate the token
    }

    /**
     * Validate the JWT token by checking its signature and expiration.
     * @param token the JWT token to validate.
     * @return true if the token is valid, false otherwise.
     */
    public boolean validateToken(String token) {
        try {
            // Parse and validate the token using the secret key
            Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            logger.error("Invalid JWT token: " + e.getMessage());
        }
        return false;
    }

    /**
     * Get the secret key from the base64 encoded string for signing and verifying the JWT.
     * @return the secret key.
     */
    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey)); // Decode and return the key
    }

    /**
     * Extract the username from the JWT token.
     * @param jwt the JWT token to extract the username from.
     * @return the username if valid, null otherwise.
     */
    public String getUserNameFromJwtToken(String jwt) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key())
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody()
                    .getSubject(); // Extract the subject (username)
        } catch (Exception e) {
            logger.error("Cannot parse JWT: {}", e.getMessage());
        }
        return null; // Return null if there's an error
    }
}
