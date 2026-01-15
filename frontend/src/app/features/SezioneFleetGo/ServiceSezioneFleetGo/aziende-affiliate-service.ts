import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {AdminAziendaleDTO} from '@core/models/AdminAziendaleDTO';
import {AziendaDTO} from '@core/models/AziendaDTO';
import {ContenitoreDatiRegistrazioneAzienda} from '@core/models/ContenitoreDatiRegistrazioneAzienda';
import {Observable} from 'rxjs';
import {environment} from '../../../../../environment';

@Injectable({
  providedIn: 'root',
})

export class AziendeAffiliateService {

  constructor(private http: HttpClient) {
  }

  private apiUrl = environment.apiUrl + '/dashboardFleetGo';

  registraAzienda(contenitoreDati: ContenitoreDatiRegistrazioneAzienda) {

    console.log('sto inviando', contenitoreDati);

    return this.http.post(`${this.apiUrl}/registraAziendaAdmin`, contenitoreDati, {
      withCredentials: true,
      responseType: 'text'
    });
  }

  richiediAziendeAttive(): Observable<AziendaDTO[]> {
    return this.http.get<AziendaDTO[]>(`${this.apiUrl}/elencoAziendeAttive`, {
      withCredentials: true
    });
  }

  richiediAziendeDisabilitate(): Observable<AziendaDTO[]> {
    return this.http.get<AziendaDTO[]>(`${this.apiUrl}/elencoAziendeDisabilitate`, {
      withCredentials: true
    });
  }

  riabilitaAzienda(idAzienda: number): Observable<string> {
    return this.http.post(`${this.apiUrl}/riabilitaAzienda`, idAzienda, {withCredentials: true, responseType: 'text'});
  }

  disabilitaAzienda(idAzienda: number): Observable<string> {
    return this.http.post(`${this.apiUrl}/disabilitaAzienda`, idAzienda, {withCredentials: true, responseType: 'text'});
  }
}
