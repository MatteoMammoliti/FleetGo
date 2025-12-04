import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {ModificaDatiUtenteDTO} from '@models/ModificaDatiUtenteDTO';

@Injectable({
  providedIn: 'root',
})

export class ModificaDatiService {

  private apiUrl = 'http://localhost:8080/dashboardAdminAziendale';

  constructor(private http: HttpClient) {}

  public getDati(): Observable<ModificaDatiUtenteDTO> {
    return this.http.get<ModificaDatiUtenteDTO>(`${this.apiUrl}/datiUtente`, { withCredentials: true });
  }

  public modificaDati(dati: ModificaDatiUtenteDTO): Observable<string> {
    return this.http.post(`${this.apiUrl}/modificaDatiAdmin`, dati, { responseType: 'text', withCredentials: true});
  }
}
