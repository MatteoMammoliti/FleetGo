import {Injectable} from '@angular/core';
import {environment} from '@env/environment';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {RichiestaNoleggioDTO} from '@core/models/RichiestaNoleggioDTO';
import {StatisticheDipendenteDTO} from '@core/models/StatisticheDipendenteDTO';
import {LuogoDTO} from '@core/models/LuogoDTO';

@Injectable({
  providedIn: 'root'
})

export class HomeService {
  private apiUrl = environment.apiUrl + '/dashboardDipendente';

  constructor(private http: HttpClient) {
  }

  richiediProssimoViaggio(): Observable<RichiestaNoleggioDTO> {
    return this.http.get<RichiestaNoleggioDTO>(`${this.apiUrl}/prossimoViaggio`, {
      withCredentials: true
    })
  }

  richiediStatisticheDipendente(): Observable<StatisticheDipendenteDTO> {
    return this.http.get<StatisticheDipendenteDTO>(`${this.apiUrl}/statisticheDipendente`, {
      withCredentials: true
    })
  }

  richiediLuoghiAzienda(): Observable<LuogoDTO[]> {
    return this.http.get<LuogoDTO[]>(`${this.apiUrl}/luoghiAzienda`, {
      withCredentials: true
    })
  }

  richiediNomeDipendente(): Observable<string> {
    return this.http.get(`${this.apiUrl}/richiediNome`, {
      responseType: 'text',
      withCredentials: true
    })
  }

  richiediPrenotazioni(): Observable<RichiestaNoleggioDTO[]> {
    return this.http.get<RichiestaNoleggioDTO[]>(`${this.apiUrl}/leMiePrenotazioni`, {
      withCredentials: true
    })
  }

  inviaSegnalazione(messaggio: string | null): Observable<string> {
    return this.http.post(`${this.apiUrl}/inviaSegnalazione`, messaggio, {
      withCredentials: true,
      responseType: 'text',
    })
  }
}
