import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '@env/environment';
import {Observable} from 'rxjs';
import {OffertaDTO} from '@core/models/OffertaDTO';

@Injectable({
  providedIn: 'root'
})

export class OfferteAttiveService {

  constructor(private http: HttpClient) {
  }

  private apiUrl = environment.apiUrl + '/dashboardFleetGo';

  inserisciNuovaOfferta(formData: FormData): Observable<string> {
    return this.http.post(`${this.apiUrl}/inserisciOfferta`, formData, {withCredentials: true, responseType: "text"});
  }

  eliminaOfferta(idOfferta: number) {
    return this.http.post(`${this.apiUrl}/eliminaOfferta`, idOfferta, {withCredentials: true, responseType: "text"})
  }

  getOfferteAttive(): Observable<OffertaDTO[]> {
    return this.http.get<OffertaDTO[]>(`${this.apiUrl}/getOfferte`, {withCredentials: true}
    )
  }
}
