import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {ModificaDatiUtenteDTO} from '@models/ModificaDatiUtenteDTO';

@Injectable({
  providedIn: 'root',
})

export class FlottaAdminAziendaleService {
  private apiUrl = 'http://localhost:8080/dashboardAdminAziendale';

  constructor(private http: HttpClient) {}

  public richiediVeicoliAziendali(): Observable<any> {
    return this.http.get(`${this.apiUrl}/flottaAziendale`, { withCredentials: true });
  }



}
