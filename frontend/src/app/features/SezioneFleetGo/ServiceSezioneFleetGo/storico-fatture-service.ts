import {Injectable} from '@angular/core';
import {environment} from '@env/environment';
import {Observable} from 'rxjs';
import {FatturaDTO} from '@core/models/FatturaDTO';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})

export class StoricoFattureService {

  constructor(private http: HttpClient) {
  }

  apiUrl = environment.apiUrl + '/dashboardFleetGo';

  public getFatturePerAnno(anno: number): Observable<FatturaDTO[]> {
    return this.http.get<FatturaDTO[]>(`${this.apiUrl}/getFatture/${anno}`, {
      withCredentials: true
    });
  }

  public downloadFattura(idFattura: number): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/downloadFattura/${idFattura}`, {
      responseType: 'blob',
      withCredentials: true
    });
  }

  public getAnniFatture(): Observable<number[]> {
    return this.http.get<number[]>(`${this.apiUrl}/getAnni`, {
      withCredentials: true
    });
  }
}
