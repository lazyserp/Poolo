package com.poolo.poolo.vehicle.service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.poolo.poolo.identity.dto.UserResponseDTO;
import com.poolo.poolo.identity.models.Role;
import com.poolo.poolo.identity.service.UserService;
import com.poolo.poolo.vehicle.dto.RegisterVehicleRequest;
import com.poolo.poolo.vehicle.dto.VehicleResponseDTO;
import com.poolo.poolo.vehicle.models.Vehicle;
import com.poolo.poolo.vehicle.models.VehicleType;
import com.poolo.poolo.vehicle.repository.VehicleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VehicleService {
    private final VehicleRepository vehicleRepository;
    private final UserService userService;

    public VehicleResponseDTO registerVehicle(RegisterVehicleRequest request) {
        
        UserResponseDTO owner = userService.getUserById(request.ownerId());

       
        if (owner.role() != Role.HOST) {
            throw new IllegalArgumentException("Only users with HOST role can register a vehicle");
        }

        
        if (!owner.corporateEmailVerified()) {
            throw new IllegalArgumentException("Host must verify corporate email before registering a vehicle");
        }

        
        if (request.type() == VehicleType.TWO_WHEELER && request.capacity() > 1) {
            throw new IllegalArgumentException("Two-wheelers cannot have more than 1 passenger seat");
        }

        
        if (vehicleRepository.existsByLicensePlate(request.licensePlate())) {
            throw new IllegalArgumentException("Vehicle with license plate already registered: " + request.licensePlate());
        }

        
        Vehicle vehicle = Vehicle.builder()
            .ownerId(request.ownerId())
            .make(request.make())
            .model(request.model())
            .color(request.color())
            .licensePlate(request.licensePlate())
            .capacity(request.capacity())
            .type(request.type())
            .createdAt(Instant.now())
            .build();

        Vehicle saved = vehicleRepository.save(vehicle);
        return VehicleResponseDTO.from(saved);
    }

    public List<VehicleResponseDTO> getVehiclesByOwner(UUID ownerId) {
        return vehicleRepository.findByOwnerId(ownerId)
            .stream()
            .map(VehicleResponseDTO::from)
            .toList();
    }

    public VehicleResponseDTO getVehicleById(UUID vehicleId){
        return  vehicleRepository.findById(vehicleId)
                                    .map(VehicleResponseDTO::from)
                                    .orElseThrow(() -> new IllegalArgumentException("Vehicle Not Found"));
    }
}
