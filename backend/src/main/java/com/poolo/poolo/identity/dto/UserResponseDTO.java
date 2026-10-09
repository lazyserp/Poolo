package com.poolo.poolo.identity.dto;

import java.time.Instant;
import java.util.UUID;

import com.poolo.poolo.identity.models.Role;

public record UserResponseDTO (
    UUID id,
    String name,
    String corporateEmail,
    String phoneNumber,
    boolean corporateEmailVerified,
    Role role,
    Instant createdAt

){}
