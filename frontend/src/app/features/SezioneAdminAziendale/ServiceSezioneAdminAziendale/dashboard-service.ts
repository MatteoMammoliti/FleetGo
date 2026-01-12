import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {environment} from '@env/environment';
import {OffertaDTO} from '@core/models/OffertaDTO';
import {ContenitoreStatisticheNumeriche} from '@core/models/ContenitoreStatisticheNumeriche';

@Injectable({
  providedIn: 'root',
})

export class DashboardService {
  private apiUrl = environment.apiUrl + '/dashboardAdminAziendale';

  constructor(private http: HttpClient) {}

  getOfferteAttive(): Observable<OffertaDTO[]> {
    return this.http.get<OffertaDTO[]>(`${this.apiUrl}/getOfferte`, {withCredentials: true})
  }

  getContatoreRichiesteAffiliazione(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/getContatoreRichiesteAffiliazione`, {withCredentials: true})
  }

  getContatoreRichiesteNoleggio(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/getContatoreRichiesteNoleggio`, {withCredentials: true})
  }

  getSpesaMensile(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/getSpesaMensile`, {withCredentials: true})
  }

  getNomeAziendaGestita(): Observable<string> {
    return this.http.get(`${this.apiUrl}/getNomeAziendaGestita`, {
      withCredentials: true,
      responseType: 'text'
    });
  }

  getNomeCognomeAdmin(): Observable<string> {
    return this.http.get(`${this.apiUrl}/getNomeCognomeAdmin`, {
      withCredentials: true,
      responseType: 'text'
    });
  }

  inoltraRichiestaDiAppuntamento(): Observable<string> {
    return this.http.post(`${this.apiUrl}/richiediAppuntamento`, {}, {withCredentials: true, responseType: "text"})
  }

  getNumFattureDaPagare(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/getNumFattureDaPagare`, {withCredentials: true})
  }

  isSedeImpostata() {
    return this.http.get<boolean>(`${this.apiUrl}/isSedeImpostata`, {withCredentials: true})
  }

  getDatiGraficoTorta(): Observable<ContenitoreStatisticheNumeriche> {
    return this.http.get<ContenitoreStatisticheNumeriche>(`${this.apiUrl}/getDatiGraficoTorta`, {withCredentials: true});
  }

  getNumeroAutoSenzaLuogo() {
    return this.http.get<number>(`${this.apiUrl}/getNumeroAutoSenzaLuogo`, {withCredentials: true});
  }
}
