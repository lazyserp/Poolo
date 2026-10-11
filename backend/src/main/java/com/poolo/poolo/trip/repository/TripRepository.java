package com.poolo.poolo.trip.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poolo.poolo.trip.models.Trip;
import com.poolo.poolo.trip.models.TripStatus;

public interface TripRepository extends JpaRepository<Trip,UUID>{
    List<Trip> findByHostId(UUID hostId);
    List<Trip> findByStatus(TripStatus status);
} 
