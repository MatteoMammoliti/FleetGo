import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class AziendeAffiliateService {
  
  constructor(private http: HttpClient) {}

  private apiUrl = 'http://localhost:8080/fleetGoDashboard';

  registraAzienda(formData: FormData){
    return this.http.post(`${this.apiUrl}/registraAzienda`, formData, { responseType: 'text' });
  }

}