package com.poolo.poolo.vehicle.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.poolo.poolo.vehicle.dto.RegisterVehicleRequest;
import com.poolo.poolo.vehicle.dto.VehicleResponseDTO;
import com.poolo.poolo.vehicle.service.VehicleService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController 
@RequestMapping("api/v1/vehicles")
@RequiredArgsConstructor 
public class VehicleController {
    private final VehicleService vehicleService;

    @PostMapping 
    @ResponseStatus(HttpStatus.CREATED)
    public VehicleResponseDTO registerVehicle(@Valid @RequestBody RegisterVehicleRequest request){
        return vehicleService.registerVehicle(request);
    }

    @GetMapping("/owner/{ownerId}")
    public List<VehicleResponseDTO> getVehiclesByOwner(@PathVariable UUID ownerId){
        return vehicleService.getVehiclesByOwner(ownerId);

    }
}

