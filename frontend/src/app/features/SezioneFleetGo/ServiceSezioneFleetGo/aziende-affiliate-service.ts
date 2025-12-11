import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {AdminAziendaleDTO} from '@core/models/adminAziendaleDTO.models';
import {AziendaDTO} from '@core/models/aziendaDTO';
import {ContenitoreDatiRegistrazioneAzienda} from '@core/models/ContenitoreDatiRegistrazioneAzienda';
import {Observable} from 'rxjs';
import {environment} from '@env/environment';

@Injectable({
  providedIn: 'root',
})

export class AziendeAffiliateService {

  constructor(private http: HttpClient) {}

  private apiUrl = environment.apiUrl+'/dashboardFleetGo';

  registraAzienda(admin: AdminAziendaleDTO, azienda: AziendaDTO){

    const contenitore:ContenitoreDatiRegistrazioneAzienda = {
      adminAziendale: admin,
      azienda: azienda
    }

    return this.http.post(`${this.apiUrl}/registraAzienda&Admin`, contenitore, { withCredentials: true,  responseType: 'text' });
  }

  richiediAziende(): Observable<AziendaDTO[]>{
    return this.http.get<AziendaDTO[]>(`${this.apiUrl}/elencoAziende`, {
      withCredentials:true
    });
  }

  eliminaAzienda(idAdmin: number | undefined): Observable<string> {
    return this.http.post(`${this.apiUrl}/eliminaAzienda`, idAdmin, {
      responseType: 'text',
      withCredentials:true
    });
  }
}
