package com.example.springboot.common;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    private final SecretKey secretKey;
    private final long expirationSeconds;

    public JwtUtil(JwtProperties jwtProperties) {
        this.secretKey = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
        this.expirationSeconds = jwtProperties.getExpirationSeconds();
    }

    public String generateToken(String username, String role, String userId, String displayName, String avatar) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        claims.put("userId", userId);
        claims.put("displayName", displayName);
        claims.put("avatar", avatar);

        Instant now = Instant.now();
        Instant expiration = now.plusSeconds(expirationSeconds);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiration))
                .signWith(secretKey)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
    }

    public String getUsernameFromToken(String token) {
        return getClaims(token).getSubject();
    }

    public String getRoleFromToken(String token) {
        return (String) getClaims(token).get("role");
    }

    public String getUserIdFromToken(String token) {
        Object value = getClaims(token).get("userId");
        return value == null ? null : String.valueOf(value);
    }

    public String getDisplayNameFromToken(String token) {
        Object value = getClaims(token).get("displayName");
        return value == null ? null : String.valueOf(value);
    }

    public String getAvatarFromToken(String token) {
        Object value = getClaims(token).get("avatar");
        return value == null ? null : String.valueOf(value);
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
