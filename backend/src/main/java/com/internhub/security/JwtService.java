package com.internhub.security;

import com.internhub.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

@Service
public class JwtService {
    private static final long EXPIRY_SECONDS = 3600;
    private final String configuredSecret;
    private SecretKey signingKey;

    public JwtService(@Value("${JWT_SECRET:}") String configuredSecret) {
        this.configuredSecret = configuredSecret;
    }

    @PostConstruct
    void initialize() {
        if (configuredSecret.isBlank() || configuredSecret.startsWith("replace_with") || configuredSecret.length() < 32) {
            throw new IllegalStateException("JWT_SECRET must be configured with at least 32 characters.");
        }
        try {
            signingKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(configuredSecret));
        } catch (IllegalArgumentException exception) {
            signingKey = Keys.hmacShaKeyFor(configuredSecret.getBytes(java.nio.charset.StandardCharsets.UTF_8));
        }
    }

    public String issue(User user) {
        Instant now = Instant.now();
        return Jwts.builder().subject(user.getId().toString()).claim("email", user.getEmail()).claim("role", user.getRole().name())
                .issuedAt(Date.from(now)).expiration(Date.from(now.plusSeconds(EXPIRY_SECONDS))).signWith(signingKey).compact();
    }

    public long expiresIn() { return Duration.ofSeconds(EXPIRY_SECONDS).toSeconds(); }

    public Long userId(String token) { return Long.valueOf(claims(token).getSubject()); }

    public boolean isValid(String token, UserDetails userDetails) {
        try { return userId(token).equals(((DatabaseUserPrincipal) userDetails).getId()) && !claims(token).getExpiration().before(new Date()) && userDetails.isEnabled(); }
        catch (RuntimeException exception) { return false; }
    }

    private Claims claims(String token) { return Jwts.parser().verifyWith(signingKey).build().parseSignedClaims(token).getPayload(); }
}
