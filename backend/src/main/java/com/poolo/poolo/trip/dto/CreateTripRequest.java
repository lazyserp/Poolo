package com.poolo.poolo.trip.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateTripRequest (
    
    @NotNull 
    UUID hostId,

    @NotNull 
    UUID vehicleId,

    @NotBlank 
    String origin,

    @NotBlank
    String destination,

    @NotNull 
    Instant departureTime,

    @Min (1)
    int totalSeats,

    @NotNull @DecimalMin (value = "0.0")
    BigDecimal pricePerSeat

){}
