package com.poolo.poolo.identity.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest (
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid Email")
    String corporateEmail,

    @NotBlank(message = "Password is required")
    String password
){}
