package com.poolo.poolo.trip.controller;

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

import com.poolo.poolo.trip.dto.CreateTripRequest;
import com.poolo.poolo.trip.dto.TripResponseDTO;
import com.poolo.poolo.trip.service.TripService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController 
@RequestMapping("/api/v1/trips")
@RequiredArgsConstructor 
public class TripController {
    private final TripService tripService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public TripResponseDTO createTrip(@Valid @RequestBody CreateTripRequest request){
        return tripService.createTrip(request);   
    }

    @GetMapping("/{id}")
    public TripResponseDTO getTrip(@Valid @PathVariable UUID id){
        return tripService.getTripById(id);
    }

    @GetMapping("/host/{hostId}")
    public List<TripResponseDTO> getTripsByHost(@Valid @PathVariable UUID hostId){
        return tripService.getTripsByHost(hostId);
    }


    
}
