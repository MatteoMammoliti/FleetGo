import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {ModificaDatiUtenteDTO} from '@core/models/ModificaDatiUtenteDTO';
import {environment} from '@env/environment';

@Injectable({
  providedIn: 'root',
})

export class FlottaAdminAziendaleService {
  private apiUrl = environment.apiUrl+'/dashboardAdminAziendale';

  constructor(private http: HttpClient) {}

  public richiediVeicoliAziendali(): Observable<any> {
    return this.http.get(`${this.apiUrl}/flottaAziendale`, { withCredentials: true });
  }

}
