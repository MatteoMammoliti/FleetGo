import {Injectable} from '@angular/core';
import {environment} from '../../../../../environment';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {ModificaDatiUtenteDTO} from '@core/models/ModificaDatiUtenteDTO';

@Injectable({
  providedIn: 'root'
})

export class ImpostazioniService {
  private apiUrl = environment.apiUrl + '/dashboardDipendente';

  constructor(private http: HttpClient) {
  }

  public getDipendente(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/dettagliUtente`, {
      withCredentials: true
    })
  }

  public getUrlPatente(): Observable<string> {
    return this.http.get(`${this.apiUrl}/getUrlPatente`, {withCredentials: true, responseType: 'text'})
  }

  public inviaModifiche(dati: ModificaDatiUtenteDTO): Observable<string> {

    console.log(dati);

    return this.http.post(`${this.apiUrl}/applicaModifiche`, dati, {
      withCredentials: true, responseType: "text"
    })
  }

  public aggiornaPatente(dati: File): Observable<string> {
    const formData = new FormData();

    formData.append('dati', dati);

    return this.http.post(`${this.apiUrl}/aggiornaPatente`, formData, {
      withCredentials: true,
      responseType: "text"
    });
  }

  public abbandonaAzienda(): Observable<any> {
    return this.http.post(`${this.apiUrl}/lasciaAzienda`, {}, {
      withCredentials: true,
      responseType: 'text'
    });
  }
}
