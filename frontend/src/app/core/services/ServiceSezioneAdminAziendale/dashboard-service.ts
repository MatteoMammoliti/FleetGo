import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {ContenitoreStatisticheNumeriche} from '@models/ContenitoreStatisticheNumeriche';

@Injectable({
  providedIn: 'root',
})

export class DashboardService {
  private apiUrl = 'http://localhost:8080/dashboardAdminAziendale';

  constructor(private http: HttpClient) {}

  public getStatoVeicoli(): Observable<ContenitoreStatisticheNumeriche> {
    return this.http.get<ContenitoreStatisticheNumeriche>(`${this.apiUrl}/statoVeicoli`, { withCredentials: true });
  }
}
