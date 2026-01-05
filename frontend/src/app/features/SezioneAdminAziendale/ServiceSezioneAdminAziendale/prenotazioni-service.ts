import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '@env/environment';
import {Observable} from 'rxjs';
import {RichiestaNoleggioDTO} from '@core/models/richiestaNoleggioDTO.models';
import {RisoluzioneConfilittiNoleggio} from '@core/models/RisoluzioneConfilittiNoleggio';

@Injectable({
  providedIn: 'root',
})

export class PrenotazioniService {
  private apiUrl = environment.apiUrl+'/dashboardAdminAziendale';

  constructor(private http: HttpClient) {}

  public getPrenotazioni():Observable<RichiestaNoleggioDTO[]> {
    return this.http.get<RichiestaNoleggioDTO[]>(`${this.apiUrl}/getPrenotazioni`, { withCredentials: true })
  }

  public getPrenotazioniDaAccettare():Observable<RichiestaNoleggioDTO[]> {
    return this.http.get<RichiestaNoleggioDTO[]>(`${this.apiUrl}/getPrenotazioniDaAccettare`, { withCredentials: true })
  }

  public getPrenotazioneDettagliata(idRichiesta: number): Observable<RichiestaNoleggioDTO> {
    return this.http.get<RichiestaNoleggioDTO>(`${this.apiUrl}/getPrenotazioni/${idRichiesta}`, { withCredentials: true })
  }

  public getNumeroNoleggiDaApprovare(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/getNumeroNoleggiDaApprovare`, { withCredentials: true })
  }

  public approvaRichiesta(idRichiesta: number): Observable<string> {
    return this.http.post(`${this.apiUrl}/approvaRichiesta`, idRichiesta, { responseType: 'text', withCredentials: true })
  }

  public rifiutaRichiesta(idRichiesta: number): Observable<string> {
    return this.http.post(`${this.apiUrl}/rifiutaRichiesta`, idRichiesta, { responseType: 'text', withCredentials: true })
  }

  public rifiutoAutomaticoRichieste(dto: RisoluzioneConfilittiNoleggio) {
    return this.http.post(`${this.apiUrl}/accettazioneConRifiuto`, dto, {responseType: 'text', withCredentials: true});
  }
}
