import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {environment} from '@env/environment';
import {VeicoloDTO} from '@core/models/veicoloDTO.model';
import {RichiestaManutenzioneDTO} from '@core/models/RichiestaManutenzioneDTO';

@Injectable({
  providedIn: 'root',
})

export class DettagliVeicoloAziendaleService {
  private apiUrl = environment.apiUrl + '/dashboardAdminAziendale';

  constructor(private http: HttpClient) {
  }

  public richiediVeicolo(targa: string | null): Observable<any> {
    return this.http.get<VeicoloDTO>(`${this.apiUrl}/informazioneVeicolo/${targa}`, {withCredentials: true});
  }

  public aggiornaPosizioneVeicolo(veicolo: VeicoloDTO): Observable<string> {
    return this.http.post(`${this.apiUrl}/impostaLuogo`, veicolo, {responseType: 'text', withCredentials: true});
  }

  public richiediManutenzioneVeicolo(idVeicolo: number) {
    return this.http.get<RichiestaManutenzioneDTO>(`${this.apiUrl}/getRichiestaManutenzione/${idVeicolo}`, {withCredentials: true});
  }

  public annullaRichiesta(richiesta: RichiestaManutenzioneDTO) {
    return this.http.post(`${this.apiUrl}/eliminaRichiesta`, richiesta, {responseType: 'text', withCredentials: true});
  }
}
