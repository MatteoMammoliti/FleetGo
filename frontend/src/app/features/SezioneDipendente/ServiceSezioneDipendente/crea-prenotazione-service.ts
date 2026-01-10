import {Injectable} from '@angular/core';
import {environment} from '@env/environment';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {RichiestaNoleggioDTO} from '@core/models/richiestaNoleggioDTO.models';
import {LuogoDTO} from '@core/models/luogoDTO.models';
import {VeicoloPrenotazioneDTO} from '@core/models/veicoloPrenotazioneDTO';

@Injectable({
  providedIn: 'root'
})

export class CreaPrenotazioneService {
  private apiUrl = environment.apiUrl + '/dashboardDipendente';

  constructor(private http: HttpClient) {
  }

  richiediLuoghi(): Observable<LuogoDTO[]> {
    return this.http.get<LuogoDTO[]>(`${this.apiUrl}/caricaLuoghi`, {
      withCredentials: true
    })
  }

  richiediVeicoli(nomeLuogo: string, dataInizio: string, dataFine: string, oraInizio: string, oraFine: string): Observable<VeicoloPrenotazioneDTO[]> {
    let params = new HttpParams()
      .set('ritiro', dataInizio)
      .set('consegna', dataFine)
      .set('oraInizio', oraInizio)
      .set('oraFine', oraFine)
      .set('nomeLuogo', nomeLuogo)
    return this.http.get<VeicoloPrenotazioneDTO[]>(`${this.apiUrl}/caricaVeicoli`, {
      params: params,
      withCredentials: true
    })
  }

  inviaRichiestaNoleggio(dati: RichiestaNoleggioDTO): Observable<string> {
    return this.http.post(`${this.apiUrl}/inviaRichiesta`, dati, {
      responseType: 'text',
      withCredentials: true
    });
  }
}
