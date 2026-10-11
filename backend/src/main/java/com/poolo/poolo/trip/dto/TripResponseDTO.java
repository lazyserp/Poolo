package com.poolo.poolo.trip.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import com.poolo.poolo.trip.models.Trip;
import com.poolo.poolo.trip.models.TripStatus;

public record TripResponseDTO(
    UUID id,
    UUID hostId,
    UUID vehicleId,
    String origin,
    String destination,
    Instant departureTime,
    int totalSeats,
    int availableSeats,
    BigDecimal pricePerSeat,
    TripStatus status,
    Instant createdAt
) {
    public static TripResponseDTO from(Trip trip) {
        return new TripResponseDTO(
            trip.getId(),
            trip.getHostId(),
            trip.getVehicleId(),
            trip.getOrigin(),
            trip.getDestination(),
            trip.getDepartureTime(),
            trip.getTotalSeats(),
            trip.getAvailableSeats(),
            trip.getPricePerSeat(),
            trip.getStatus(),
            trip.getCreatedAt()
        );
    }
}
