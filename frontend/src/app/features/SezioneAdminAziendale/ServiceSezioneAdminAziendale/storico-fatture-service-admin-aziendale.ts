import {Injectable} from '@angular/core';
import {environment} from '@env/environment';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {FatturaDTO} from '@core/models/FatturaDTO';

@Injectable({
  providedIn: 'root'
})

export class StoricoFattureServiceAdminAziendale {

  private apiUrl = environment.apiUrl + '/dashboardAdminAziendale';

  constructor(private http: HttpClient) {
  }

  getFattureEmesse(): Observable<FatturaDTO[]> {
    return this.http.get<FatturaDTO[]>(`${this.apiUrl}/getFattureEmesse`, {withCredentials: true})
  }

  public downloadFattura(idFattura: number): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/downloadFattura/${idFattura}`, {
      responseType: 'blob',
      withCredentials: true
    });
  }

  public pagaFattura(idFattura: number): Observable<string> {
    return this.http.post(
      `${this.apiUrl}/pagaFattura/${idFattura}`, {}, {responseType: 'text', withCredentials: true}
    );
  }

  public contrassegnaFatturaPagata(idFattura: number): Observable<any> {
    return this.http.post(`${this.apiUrl}/fatturaPagata/${idFattura}`, {}, {withCredentials: true, responseType: 'text'})
  }

  public getAnniDisponibili(): Observable<number[]> {
    return this.http.get<number[]>(`${this.apiUrl}/getAnniDisponibili`, {withCredentials: true});
  }
}
