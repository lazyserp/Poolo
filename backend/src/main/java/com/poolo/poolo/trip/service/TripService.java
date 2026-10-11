package com.poolo.poolo.trip.service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.poolo.poolo.trip.dto.CreateTripRequest;
import com.poolo.poolo.trip.dto.TripResponseDTO;
import com.poolo.poolo.trip.models.Trip;
import com.poolo.poolo.trip.models.TripStatus;
import com.poolo.poolo.trip.repository.TripRepository;
import com.poolo.poolo.vehicle.dto.VehicleResponseDTO;
import com.poolo.poolo.vehicle.service.VehicleService;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class TripService {
    private final VehicleService vehicleService;
    private final TripRepository tripRepository;

    public void isValidTime(CreateTripRequest tripRequest){
        if ( !tripRequest.departureTime().isAfter(Instant.now())){
            throw new IllegalArgumentException("Trip departure time must be in future");
        }
    }
    public void isValidVehicle(CreateTripRequest tripRequest, VehicleResponseDTO vehicle){
        if (!vehicle.ownerId().equals(tripRequest.hostId())){
            throw new IllegalArgumentException("Vehicle does not belong to the host");
        }
    }
    public void isValidSeatCount(CreateTripRequest tripRequest, VehicleResponseDTO vehicle){
        if( vehicle.capacity() < tripRequest.totalSeats()){
            throw new IllegalArgumentException("Offered seats (" + tripRequest.totalSeats() +  ") cannot exceed vehicle capacity (" + vehicle.capacity() + ")");

        }
    }

    public TripResponseDTO createTrip(CreateTripRequest request){

        VehicleResponseDTO vehicle = vehicleService.getVehicleById(request.vehicleId());
        isValidTime(request);
        isValidSeatCount(request, vehicle);
        isValidVehicle(request, vehicle);


        Trip trip = Trip.builder()
                        .hostId(request.hostId())
                        .vehicleId(request.vehicleId())
                        .origin(request.origin())
                        .destination(request.destination())
                        .departureTime(request.departureTime())
                        .totalSeats(request.totalSeats())
                        .availableSeats(request.totalSeats()) 
                        .pricePerSeat(request.pricePerSeat())
                        .status(TripStatus.SCHEDULED)
                        .createdAt(Instant.now())
                        .build();
        
        Trip saved = tripRepository.save(trip);

        return TripResponseDTO.from(saved);
        
    }

    public List<TripResponseDTO> getTripsByHost(UUID hostId){
        List<TripResponseDTO> trips = tripRepository.findByHostId(hostId)
                                                    .stream()
                                                    .map(TripResponseDTO::from)
                                                    .toList();
        return trips;
    }

    public TripResponseDTO getTripById(UUID id){
        return tripRepository.findById(id)
                             .map(TripResponseDTO::from)
                             .orElseThrow(() -> new IllegalArgumentException("No trips Found with ID: " + id));
    }

    public Slice<TripResponseDTO> searchTrips(String origin , String destination , Instant departureAfter,int minSeats,int page , int size){
        Instant searchTime = (departureAfter != null) ? departureAfter : Instant.now();
        int seats = (minSeats > 0) ? minSeats : 1;

        Pageable pageable = PageRequest.of(page, size,Sort.by("departureTime").ascending());

        return tripRepository.findByOriginIgnoreCaseAndDestinationIgnoreCaseAndStatusAndDepartureTimeAfterAndAvailableSeatsGreaterThanEqual(
            origin, destination, TripStatus.SCHEDULED, searchTime, seats, pageable).map((trip) -> TripResponseDTO.from(trip));
    }
}
