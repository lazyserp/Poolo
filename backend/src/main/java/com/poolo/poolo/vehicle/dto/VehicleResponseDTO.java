package com.poolo.poolo.vehicle.dto;

import java.time.Instant;
import java.util.UUID;
import com.poolo.poolo.vehicle.models.Vehicle;
import com.poolo.poolo.vehicle.models.VehicleType;


public record VehicleResponseDTO(
    UUID id,
    UUID ownerId,
    String make,
    String model,
    String color,
    String licensePlate,
    int capacity,
    VehicleType type,
    Instant createdAt
    
){

public static VehicleResponseDTO from(Vehicle vehicle) {
        return new VehicleResponseDTO(
            vehicle.getId(),
            vehicle.getOwnerId(),
            vehicle.getMake(),
            vehicle.getModel(),
            vehicle.getColor(),
            vehicle.getLicensePlate(),
            vehicle.getCapacity(),
            vehicle.getType(),
            vehicle.getCreatedAt()
            
        );
}
}