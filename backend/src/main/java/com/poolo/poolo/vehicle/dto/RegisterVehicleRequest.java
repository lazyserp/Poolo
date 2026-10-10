package com.poolo.poolo.vehicle.dto;

import java.util.UUID;

import com.poolo.poolo.vehicle.models.VehicleType;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record RegisterVehicleRequest (
    @NotNull(message = "Owner ID is required")
    UUID ownerId,

    @NotNull(message = "Make is required")
    String make,

    @NotNull(message = "Model is required")
    String model,

    String color,

    @NotNull(message = "License plate is required")
    String licensePlate,


    @Min(value = 1, message = "Capacity must be atleast 1")
    @Max(value = 4, message = "Capacity cannot exceed 4")
    int capacity,

    VehicleType type
){}
