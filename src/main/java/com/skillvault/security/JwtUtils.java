package com.skillvault.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {

    @Value("${skillvault.app.jwtSecret}")
    private String jwtSecret;

    @Value("${skillvault.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    // ✅ FIXED KEY METHOD (VERY IMPORTANT)
    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    // ✅ GENERATE TOKEN
    public String generateJwtToken(Authentication authentication) {

        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    // ✅ EXTRACT USERNAME
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)   // ✅ IMPORTANT FIX
                .getBody()
                .getSubject();
    }

    // ✅ VALIDATE TOKEN (FIXED)
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key())
                    .build()
                    .parseClaimsJws(authToken);   // ✅ IMPORTANT FIX

            return true;

        } catch (MalformedJwtException e) {
            System.err.println("Invalid JWT token: " + e.getMessage());
        } catch (ExpiredJwtException e) {
            System.err.println("JWT expired: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            System.err.println("JWT unsupported: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("JWT empty: " + e.getMessage());
        }

        return false;
    }
}