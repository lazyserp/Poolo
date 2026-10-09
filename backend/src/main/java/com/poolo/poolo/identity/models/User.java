package com.poolo.poolo.identity.models;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter 
@Setter 
@Builder 
@AllArgsConstructor 
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id 
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    @NotBlank
    @Column(unique = true, nullable = false)
    private String corporateEmail;
    private String phoneNumber;
    private String passwordHash;
    private boolean corporateEmailVerified;

    @Enumerated(EnumType.STRING)
    private Role role;
    private Instant createdAt;
    private Instant updatedAt;
}
