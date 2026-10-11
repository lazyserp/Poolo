package com.poolo.poolo.trip.models;

import java.math.BigDecimal;
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
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity 
@Table (name = "trip")
@Getter 
@Setter 
@Builder 
@NoArgsConstructor 
@AllArgsConstructor 
public class Trip {
    @Id 
    @GeneratedValue (strategy = GenerationType.UUID)
    UUID id;

    @Column (nullable = false)
    UUID hostId;

    @Column (nullable = false)
    UUID vehicleId;

    @NotNull 
    String origin;

    @NotNull 
    String destination;

    @NotNull 
    Instant departureTime;

    @NotNull 
    int totalSeats;

    @NotNull 
    int availableSeats;

    @NotNull 
    BigDecimal pricePerSeat;

    @NotNull 
    @Enumerated (EnumType.STRING)
    TripStatus status;

    @NotNull 
    Instant createdAt;
}
