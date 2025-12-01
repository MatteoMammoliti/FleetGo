import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class FlottaGlobaleService {
  constructor(private http: HttpClient) {
  }
  private apiUrl= 'http://localhost:8080/fleetGoDashboard';

  registraVeicolo(formData: FormData)   {

    return this.http.post(`${this.apiUrl}/registraVeicolo`, formData, { responseType: 'text' });
  }

}
