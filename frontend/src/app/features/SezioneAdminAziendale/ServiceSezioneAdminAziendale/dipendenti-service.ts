import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {DipendenteDTO} from '@core/models/DipendenteDTO';
import {environment} from '@env/environment';
import {RichiestaNoleggioDTO} from '@core/models/RichiestaNoleggioDTO';
import {RichiestaAffiliazioneAziendaDTO} from '@core/models/RichiestaAffiliazioneAziendaDTO';

@Injectable({
  providedIn: 'root',
})

export class DipendentiService {
  private apiUrl = environment.apiUrl + '/dashboardAdminAziendale';

  constructor(private http: HttpClient) {
  }

  public getDipendenti(): Observable<DipendenteDTO[]> {
    return this.http.get<DipendenteDTO[]>(`${this.apiUrl}/getDipendenti`, {withCredentials: true});
  }

  public rimuoviDipendente(idUtente: number | undefined): Observable<any> {
    return this.http.post(`${this.apiUrl}/rimuoviDipendente`, idUtente, {responseType: 'text', withCredentials: true});
  }

  public getRichiesteNoleggio(idDipendente: number | undefined): Observable<RichiestaNoleggioDTO[]> {
    return this.http.get<RichiestaNoleggioDTO[]>(`${this.apiUrl}/getRichiesteNoleggio/${idDipendente}`, {withCredentials: true});
  }

  public getRichiesteAffiliazione(): Observable<RichiestaAffiliazioneAziendaDTO[]> {
    return this.http.get<RichiestaAffiliazioneAziendaDTO[]>(`${this.apiUrl}/getRichiesteAffiliazione`, {withCredentials: true});
  }

  public rispondiRichiesta(idDipendente: number, risposta: boolean): Observable<string> {
    return this.http.post(`${this.apiUrl}/rispondiAffiliazione/${idDipendente}`, risposta, {
      responseType: 'text',
      withCredentials: true
    });
  }
}
