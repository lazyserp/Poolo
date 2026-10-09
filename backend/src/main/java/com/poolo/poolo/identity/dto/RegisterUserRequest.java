package com.poolo.poolo.identity.dto;

import com.poolo.poolo.identity.models.Role;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegisterUserRequest(
    @NotBlank(message = "Name is required")
    String name,

    @NotBlank(message = "Email is required")
    String corporateEmail,

    @NotBlank(message = "Password  is required")
    String password,

    @NotBlank(message = "Phone number is required")
    String phoneNumber,

    @NotNull(message = "Role is required")
    Role role
){}