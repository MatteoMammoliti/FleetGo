import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {environment} from '../../../../../environment';
import {LuogoDTO} from '@core/models/LuogoDTO';
import {RichiestaManutenzioneDTO} from '@core/models/RichiestaManutenzioneDTO';

@Injectable({
  providedIn: 'root',
})

export class FlottaAdminAziendaleService {
  private apiUrl = environment.apiUrl + '/dashboardAdminAziendale';

  constructor(private http: HttpClient) {
  }

  public richiediVeicoliAziendali(): Observable<any> {
    return this.http.get(`${this.apiUrl}/flottaAziendale`, {withCredentials: true});
  }

  public richiediLuoghi(): Observable<any[]> {
    return this.http.get<LuogoDTO[]>(`${this.apiUrl}/luoghiAzienda`, {withCredentials: true});
  }

  public inviaRichiestaManutenzione(richiesta: RichiestaManutenzioneDTO): Observable<string> {
    console.log(richiesta);
    return this.http.post(`${this.apiUrl}/inserisciRichiestaManutenzione`, richiesta, {
      responseType: 'text',
      withCredentials: true
    });
  }
}
