package com.poolo.poolo.vehicle.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poolo.poolo.vehicle.models.Vehicle;

public interface VehicleRepository extends JpaRepository<Vehicle,UUID>{

    boolean existsByLicensePlate(String licensePlate);
    List<Vehicle> findByOwnerId(UUID ownerId);

} ;
