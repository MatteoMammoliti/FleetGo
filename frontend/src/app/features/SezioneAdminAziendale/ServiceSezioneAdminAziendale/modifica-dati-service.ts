import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {ModificaDatiUtenteDTO} from '@core/models/ModificaDatiUtenteDTO';
import {environment} from '@env/environment';
import {LuogoDTO} from '@core/models/luogoDTO.models';

@Injectable({
  providedIn: 'root',
})

export class ModificaDatiService {

  private apiUrl = environment.apiUrl+'/dashboardAdminAziendale';

  constructor(private http: HttpClient) {}

  public getDati(): Observable<ModificaDatiUtenteDTO> {
    return this.http.get<ModificaDatiUtenteDTO>(`${this.apiUrl}/datiUtente`, { withCredentials: true });
  }

  public modificaDati(dati: ModificaDatiUtenteDTO): Observable<string> {
    return this.http.post(`${this.apiUrl}/modificaDatiAdmin`, dati, { responseType: 'text', withCredentials: true});
  }

  public getLuoghi(): Observable<LuogoDTO[]> {
    return this.http.get<LuogoDTO[]>(`${this.apiUrl}/luoghiAzienda`, { withCredentials: true });
  }

  public aggiungiLuogo(luogo: LuogoDTO): Observable<string> {
    return this.http.post(`${this.apiUrl}/aggiungiLuogo`,luogo, { responseType: "text", withCredentials: true });
  }

  public impostaSedeAzienda(idLuogo: number) {
    return this.http.post(`${this.apiUrl}/impostaSede`,idLuogo, { responseType: "text", withCredentials: true });
  }

  public eliminaLuogo(idLuogo: number) {
    return this.http.post(`${this.apiUrl}/eliminaLuogo`,idLuogo, { responseType: "text", withCredentials: true });
  }
}
