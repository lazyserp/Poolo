package com.poolo.poolo.trip.repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;

import com.poolo.poolo.trip.models.Trip;
import com.poolo.poolo.trip.models.TripStatus;

public interface TripRepository extends JpaRepository<Trip,UUID>{
    List<Trip> findByHostId(UUID hostId);
    List<Trip> findByStatus(TripStatus status);

    Slice<Trip> findByOriginIgnoreCaseAndDestinationIgnoreCaseAndStatusAndDepartureTimeAfterAndAvailableSeatsGreaterThanEqual(
                String origin,
                String destination,
                TripStatus status,
                Instant departureTime,
                int minSeats,
                Pageable pageable
            );

} 
