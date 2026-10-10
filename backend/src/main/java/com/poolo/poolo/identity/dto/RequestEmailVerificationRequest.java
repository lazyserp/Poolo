package com.poolo.poolo.identity.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RequestEmailVerificationRequest(
    @NotBlank(message = "Corporate Email is required")
    @Email(message = "Invalid Email format")
    String corporateEmail

) {}
