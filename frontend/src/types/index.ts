// String Unions for domain enums
export type Role = 'HOST' | 'POOLER' | 'ADMIN';
export type VehicleType = 'CAR' | 'TWO_WHEELER';
export type TripStatus = 'SCHEDULED' | 'IN_PROGRESS' | 'COMPLETED' | 'CANCELLED';

// Core Domain Entities (Matching Spring Boot Models)
export interface User {
  id: string;
  name: string;
  corporateEmail: string;
  phoneNumber: string;
  role: Role;
  corporateEmailVerified: boolean;
}

export interface Vehicle {
  id: string;
  ownerId: string;
  make: string;
  model: string;
  color: string;
  licensePlate: string;
  capacity: number;
  type: VehicleType;
}

export interface Trip {
  id: string;
  hostId: string;
  vehicleId: string;
  origin: string;
  destination: string;
  departureTime: string; // ISO-8601 UTC string (e.g. 2026-10-20T09:00:00Z)
  totalSeats: number;
  availableSeats: number;
  pricePerSeat: number;
  status: TripStatus;
}

// Authentication & DTO Contracts
export interface AuthResponse {
  accessToken: string;
  refreshToken: string;
  user: User;
}

export interface RegisterRequest {
  name: string;
  corporateEmail: string;
  password: string;
  phoneNumber: string;
  role: Role;
}

export interface LoginRequest {
  corporateEmail: string;
  password: string;
}

export interface RegisterVehicleRequest {
  ownerId: string;
  make: string;
  model: string;
  color: string;
  licensePlate: string;
  capacity: number;
  type: VehicleType;
}

export interface CreateTripRequest {
  hostId: string;
  vehicleId: string;
  origin: string;
  destination: string;
  departureTime: string;
  totalSeats: number;
  pricePerSeat: number;
}

// Spring Data Slice<T> Pagination Contract
export interface Slice<T> {
  content: T[];
  number: number;         // Current page index (0-based)
  size: number;           // Page size
  numberOfElements: number;
  first: boolean;
  last: boolean;
  hasNext: boolean;
  hasPrevious: boolean;
}
