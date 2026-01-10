import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {ContenitoreStatisticheNumeriche} from '@core/models/ContenitoreStatisticheNumeriche';
import {environment} from '@env/environment';
import {FatturaDaGenerareDTO} from '@core/models/FatturaDaGenerareDTO';
import {RichiestaManutenzioneDTO} from '@core/models/RichiestaManutenzioneDTO';
import {OffertaDTO} from '@core/models/offertaDTO.models';


@Injectable({
  providedIn: 'root'
})

export class DashboardFleetGoService {
  constructor(private http: HttpClient) {
  }

  private apiUrl = environment.apiUrl + '/dashboardFleetGo';

  richiediStatistiche(): Observable<ContenitoreStatisticheNumeriche> {
    return this.http.get<ContenitoreStatisticheNumeriche>(`${this.apiUrl}/statistiche`, {
      withCredentials: true
    });
  }

  richiediFattureDaGenerare(): Observable<FatturaDaGenerareDTO[]> {
    return this.http.get<FatturaDaGenerareDTO[]>(`${this.apiUrl}/fattureDaGenerare`, {
      withCredentials: true
    });
  }

  richiediManutenzioneDaGestire(): Observable<RichiestaManutenzioneDTO[]> {
    return this.http.get<RichiestaManutenzioneDTO[]>(`${this.apiUrl}/richiesteManutezioneDaAccettare`, {
      withCredentials: true
    });
  }

  richiediInformazioniSuManutenzioneDaGestire(idManutenzione: number): Observable<RichiestaManutenzioneDTO> {
    return this.http.get<RichiestaManutenzioneDTO>(`${this.apiUrl}/dettagliRichiestaManutenzone/${idManutenzione}`, {withCredentials: true});
  }

  accettaRichiestaManutenzione(idManutenzione: number): Observable<string> {
    return this.http.post(`${this.apiUrl}/accettaRichiestaManutenzione/${idManutenzione}`, {}, {
      withCredentials: true,
      responseType: "text"
    });
  }

  rifiutaRichiestaManutezione(idManutenzione: number): Observable<string> {
    return this.http.post(`${this.apiUrl}/rifiutaRichiestaManutenzione/${idManutenzione}`, {}, {
      withCredentials: true,
      responseType: "text"
    });
  }

  generaFattura(fattura: FatturaDaGenerareDTO): Observable<string> {
    return this.http.post(`${this.apiUrl}/generaFattura`, fattura, {
      responseType: 'text',
      withCredentials: true
    })
  }

  getOfferteAttive(): Observable<OffertaDTO[]> {
    return this.http.get<OffertaDTO[]>(`${this.apiUrl}/getOfferte`, {withCredentials: true})
  }
}
