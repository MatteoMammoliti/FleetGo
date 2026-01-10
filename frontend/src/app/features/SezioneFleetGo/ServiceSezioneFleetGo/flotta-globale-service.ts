import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {VeicoloDTO} from '@core/models/veicoloDTO.model';
import {Observable} from 'rxjs';
import {environment} from '@env/environment';
import {ModelloDTO} from '@core/models/ModelloDTO';

@Injectable({
  providedIn: 'root',
})
export class FlottaGlobaleService {
  constructor(private http: HttpClient) {
  }

  private apiUrl = environment.apiUrl + '/dashboardFleetGo';

  registraVeicolo(veicolo: VeicoloDTO): Observable<string> {
    return this.http.post(`${this.apiUrl}/registraVeicolo`, veicolo, {
      responseType: 'text',
      withCredentials: true
    });
  }

  richiediVeicoli(): Observable<VeicoloDTO[]> {
    return this.http.get<VeicoloDTO[]>(`${this.apiUrl}/listaVeicoli`, {
      withCredentials: true
    });
  }

  rimuoviVeicolo(targaVeicolo: string): Observable<string> {
    return this.http.post(`${this.apiUrl}/eliminaVeicolo`, targaVeicolo, {responseType: "text", withCredentials: true});
  }

  richiediVeicolo(targa: string | null): Observable<VeicoloDTO> {
    return this.http.get<VeicoloDTO>(`${this.apiUrl}/informazioneVeicolo/${targa}`, {withCredentials: true});
  }

  associaVeicoloAzienda(veicolo: VeicoloDTO) {
    return this.http.post(`${this.apiUrl}/associaVeicoloAzienda`, veicolo, {
      responseType: "text",
      withCredentials: true
    });
  }

  dissociaVeicoloAzienda(veicolo: VeicoloDTO) {
    return this.http.post(`${this.apiUrl}/disassociaVeicoloAzienda`, veicolo, {
      responseType: "text",
      withCredentials: true
    });
  }

  richiediModelli(): Observable<ModelloDTO[]> {
    return this.http.get<ModelloDTO[]>(`${this.apiUrl}/getModelli`, {withCredentials: true});
  }

  registraModello(formData: FormData) {
    return this.http.post(`${this.apiUrl}/registraModello`, formData, {responseType: "text", withCredentials: true});
  }

  eliminaModello(idModello: number) {
    return this.http.post(`${this.apiUrl}/eliminaModello`, idModello, {responseType: "text", withCredentials: true});
  }
}
