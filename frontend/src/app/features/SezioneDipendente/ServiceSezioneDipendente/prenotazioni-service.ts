import {Injectable} from '@angular/core';
import {environment} from '@env/environment';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {RichiestaNoleggioDTO} from '@core/models/RichiestaNoleggioDTO';

@Injectable({
  providedIn: 'root'
})

export class PrenotazioniService {
  private apiUrl = environment.apiUrl + '/dashboardDipendente';

  constructor(private http: HttpClient) {
  }

  richiediPrenotazioniDipendente(): Observable<RichiestaNoleggioDTO[]> {
    return this.http.get<RichiestaNoleggioDTO[]>(`${this.apiUrl}/leMiePrenotazioni`, {
      withCredentials: true
    })
  }

  eliminaPrenotazione(idRichiesta: number): Observable<any> {
    return this.http.post(`${this.apiUrl}/eliminaRichiesta`, idRichiesta, {
      withCredentials: true
    })
  }
}
