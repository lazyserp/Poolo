import type {
  User,
  AuthResponse,
  RegisterRequest,
  LoginRequest,
  RegisterVehicleRequest,
  Vehicle,
  CreateTripRequest,
  Trip,
  Slice,
} from "../types";

const BASE_URL : string = 'http://localhost:8080/api/v1';

export const getAccessToken = (): string | null => {
    return localStorage.getItem('poolo_access_token');
}

export const getAuthUser = () : User | null => {
    const user = localStorage.getItem('poolo_user');
    return user ? JSON.parse(user) : null ;
}

export const setAuthSession = (auth: AuthResponse): void =>{
    const poolo_access_token = auth.accessToken;
    localStorage.setItem("poolo_access_token",poolo_access_token);

    const poolo_refresh_token = auth.refreshToken;
    localStorage.setItem("poolo_refresh_token",poolo_refresh_token);

    const user = JSON.stringify(auth.user);
    localStorage.setItem("poolo_user",user);
}

export const clearAuthSession = (): void => {
  localStorage.removeItem('poolo_access_token');
  localStorage.removeItem('poolo_refresh_token');
  localStorage.removeItem('poolo_user');
}

async function request<T>(endpoint: string, options: RequestInit = {}) : Promise<T> {
    const token = getAccessToken();

    const headers : Record<string,string> = {
        'Content-Type' : 'application/json',
        ...(options.headers as Record<string,string>),
    };
    
    if ( token ){
        headers['Authorization'] = `Bearer ${token}`;
    }

    const response = await fetch(`${BASE_URL}${endpoint}`, {...options,headers,});

    if ( !response.ok) {
        const errorBody = await response.json().catch(() => ({}));
        throw new Error(errorBody.message || `HTTP error ${response.status}`);
    }
    if( response.status === 204){
        return {} as T;
    }

    return response.json();
}

export const api = {
    register : (data : RegisterRequest) =>{
        return request<User>('/auth/register', 
            {
                method: 'POST',
                body: JSON.stringify(data)
            }
        )
    },

    requestOtp: (corporateEmail : string) => {
        return request<{message : string}>(`/auth/verify-email/request?corporateEmail=${encodeURIComponent(corporateEmail)}`,
        { method : 'POST' });
    },

    confirmOtp: (corporateEmail : string, token: string) => {
        return request<{message : string}>(`/auth/verify-email/confirm?corporateEmail=${encodeURIComponent(corporateEmail)}&token=${encodeURIComponent(token)}`,
    {method : 'POST'});
    },

    login: async (data: LoginRequest): Promise<AuthResponse> => {
        const res = await request<AuthResponse>('/auth/login',
            {
                method: 'POST',
                body: JSON.stringify(data),
            }
        )
        setAuthSession(res);
        return res;
    },

    logout : () => {
        clearAuthSession();
    },

    registerVehicle: (data: RegisterVehicleRequest) => {
        return request<Vehicle>('/vehicles',
            {
                method: 'POST',
                body: JSON.stringify(data)
            }
        )
    },

    getVehiclesByOwner: (ownerId: string) => {
        return request<Vehicle[]>(`/vehicles/owner/${ownerId}`,
        )
    },

    createTrip : (data: CreateTripRequest) => {
        return request<Trip>('/trips',
            {
                method: 'POST',
                body: JSON.stringify(data)
            }
        )
    },

  getTripById: (id: string) => request<Trip>(`/trips/${id}`),

  getTripsByHost: (hostId: string) => request<Trip[]>(`/trips/host/${hostId}`),

  searchTrips: (origin: string, destination: string, page=0, size = 10) => {
    return request<Slice<Trip>>(`/trips/search?origin=${encodeURIComponent(origin)}&destination=${encodeURIComponent(destination)}&page=${page}&size=${size}`)
  }



  
}