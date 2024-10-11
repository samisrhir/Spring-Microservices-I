package com.microservices.products.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class JwtTokenFilter extends OncePerRequestFilter {

    @Value("${jwt.secretKey}") // Secret key for signing the JWT
    private String secretKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);  // Remove "Bearer "
        try {
            // Parse the JWT token
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)  // Use your secret key
                    .parseClaimsJws(token)      // Parse the token
                    .getBody();

            // Get the username from the 'sub' claim (subject)
            String username = claims.getSubject();

            // Retrieve the roles from the claims (assuming they are stored as a list)
            List<String> roles = (List<String>) claims.get("roles", List.class)
                    .stream()
                    .map(role -> ((Map<String, String>) role).get("authority"))
                    .collect(Collectors.toList());

            // Convert roles into a list of granted authorities
            List<SimpleGrantedAuthority> authorities = roles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            // Create an authentication token with roles
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(username, null, authorities);

            // Set the authentication in the security context
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Log the username and roles
            log.info("User '{}' logged in with roles: {}", username, roles);

        } catch (Exception e) {
            // Invalid token handling
            log.error("Invalid JWT Token", e);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid Token");
            return;
        }

        // Continue the filter chain
        filterChain.doFilter(request, response);
    }

}
