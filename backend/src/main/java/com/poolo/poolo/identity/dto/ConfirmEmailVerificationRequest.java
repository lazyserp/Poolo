package com.poolo.poolo.identity.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ConfirmEmailVerificationRequest (
    @NotBlank (message = "Corporate Email is required")
    @Email (message = "Invalid Email format")
    String corporateEmail,

    @NotBlank (message = "OTP is required")
    @Size (min = 6, max = 6, message = "OTP must be of 6 digits only")
    String otp
){}
