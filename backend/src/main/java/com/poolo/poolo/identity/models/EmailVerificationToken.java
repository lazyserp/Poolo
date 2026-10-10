package com.poolo.poolo.identity.models;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity 
@Table(name = "email_verification_token")
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder 
public class EmailVerificationToken {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private  UUID id;

    @Column(nullable = false, unique = true)
    private String corporateEmail;

    @Column(nullable = false)
    private String otp;

    @Column(nullable = false)
    private Instant expiresAt;

    private int attempts;

    private Instant createdAt;


    public boolean isExpired(){
        return Instant.now().isAfter(this.expiresAt);
    }

    public boolean isMaxAttemptReached(){
        return this.attempts >= 5;
    }
}
