import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {environment} from '@env/environment';
import {OffertaDTO} from '@core/models/offertaDTO.models';

@Injectable({
  providedIn: 'root',
})

export class DashboardService {
  private apiUrl = environment.apiUrl+'/dashboardAdminAziendale';

  constructor(private http: HttpClient) {}

  getOfferteAttive(): Observable<OffertaDTO[]> {
    return this.http.get<OffertaDTO[]>(`${this.apiUrl}/getOfferte`, { withCredentials:true })
  }

  getContatoreRichiesteAffiliazione(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/getContatoreRichiesteAffiliazione`, { withCredentials:true })
  }

  getContatoreRichiesteNoleggio(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/getContatoreRichiesteNoleggio`, { withCredentials:true })
  }
}
