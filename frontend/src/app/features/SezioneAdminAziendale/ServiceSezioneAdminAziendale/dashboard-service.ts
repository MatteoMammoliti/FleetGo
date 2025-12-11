import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {ContenitoreStatisticheNumeriche} from '@core/models/ContenitoreStatisticheNumeriche';
import {environment} from '@env/environment';

@Injectable({
  providedIn: 'root',
})

export class DashboardService {
  private apiUrl = environment.apiUrl+'/dashboardAdminAziendale';

  constructor(private http: HttpClient) {}

  public getStatoVeicoli(): Observable<ContenitoreStatisticheNumeriche> {
    return this.http.get<ContenitoreStatisticheNumeriche>(`${this.apiUrl}/statoVeicoli`, { withCredentials: true });
  }
}
