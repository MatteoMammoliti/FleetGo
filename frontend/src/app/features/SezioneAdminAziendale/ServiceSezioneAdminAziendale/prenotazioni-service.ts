import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '@env/environment';
import {Observable} from 'rxjs';
import {RichiestaNoleggioDTO} from '@core/models/richiestaNoleggioDTO.models';

@Injectable({
  providedIn: 'root',
})

export class PrenotazioniService {
  private apiUrl = environment.apiUrl+'/dashboardAdminAziendale';

  constructor(private http: HttpClient) {}

  public getPrenotazioni():Observable<RichiestaNoleggioDTO[]> {
    return this.http.get<RichiestaNoleggioDTO[]>(`${this.apiUrl}/getPrenotazioni`, { withCredentials: true })
  }

  public getPrenotazioneDettagliata(idRichiesta: number): Observable<RichiestaNoleggioDTO> {
    return this.http.get<RichiestaNoleggioDTO>(`${this.apiUrl}/getPrenotazioni/${idRichiesta}`, { withCredentials: true })
  }
}
