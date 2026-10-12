package com.poolo.poolo.identity.service;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import javax.crypto.SecretKey;
import com.poolo.poolo.identity.models.Role; 

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;

@Service 
public class JwtService{

    @Value("${poolo.jwt.secret}")
    private String secretKey;

    @Value("${poolo.jwt.access-token-expiration-minutes:15}")
    private long accessTokenExpirationMinutes;

    @Value("${poolo.jwt.refresh-token-expiration-days:7}")
    private long refreshTokenExpirationDays;

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateAccessToken(UUID userId, String email, Role role){
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(userId.toString())
                .claim("email", email)
                .claim("role", role.name())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(Duration.ofMinutes(accessTokenExpirationMinutes))))
                .signWith(getSigningKey())
                .compact();
    }

    public String generateRefreshToken(UUID userId){
        Instant now = Instant.now();
        return Jwts.builder()
                    .subject(userId.toString())
                    .issuedAt(Date.from(now))
                    .expiration(Date.from(now.plus(Duration.ofDays(refreshTokenExpirationDays))))
                    .signWith(getSigningKey())
                    .compact();
    }

    public Claims extractAllClaims(String token){
        return Jwts.parser()
                   .verifyWith(getSigningKey())
                   .build()
                   .parseSignedClaims(token)
                   .getPayload();
    }

    public UUID extractUserId(String token){
        return UUID.fromString(extractAllClaims(token).getSubject());
    }

    public boolean isTokenValid(String token){
        try{
            extractAllClaims(token);
            return true;
        } catch (Exception e){
            return false;
        }
    }
}