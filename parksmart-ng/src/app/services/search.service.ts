import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface ParkingCard {
  spaceId: number; name: string; type: string; location: string; city: string;
  rating: number; countRating: number; distance: number; facilities: string[];
  pricePerHour: number; latitude: number; longitude: number;
}
export interface Suggestion {
  label: string;
  latitude: number;
  longitude: number;
}
export interface SpaceSearchRequest {
  latitude: number;
  longitude: number;
  radius?: number;
  arrival?: string;
  leaving?: string;
  types?: string[];
  minPrice?: number;
  maxPrice?: number;
  amenities?: string[];
  sortBy?: string;
  page?: number;
  limit?: number;
}

@Injectable({ providedIn: 'root' })
export class SearchService {
  constructor(private http: HttpClient) {}
  suggest(query: string): Observable<Suggestion[]> {
    return this.http.post<Suggestion[]>('/api/search/location/suggest', { query }, { withCredentials: true });
  }
  search(req: SpaceSearchRequest): Observable<ParkingCard[]> {
    return this.http.post<ParkingCard[]>('/api/search/spaces/search', req, { withCredentials: true });
  }
}
