import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {AdminAziendaleDTO} from '@models/adminAziendaleDTO.models';
import {AziendaDTO} from '@models/aziendaDTO';
import {ContenitoreDatiRegistrazioneAzienda} from '@models/ContenitoreDatiRegistrazioneAzienda';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root',
})

export class AziendeAffiliateService {

  constructor(private http: HttpClient) {}

  private apiUrl = 'http://localhost:8080/dashboardFleetGo';

  registraAzienda(admin: AdminAziendaleDTO, azienda: AziendaDTO){

    const contenitore:ContenitoreDatiRegistrazioneAzienda = {
      adminAziendale: admin,
      azienda: azienda
    }

    return this.http.post(`${this.apiUrl}/registraAzienda&Admin`, contenitore, { responseType: 'text' });
  }

  richiediAziende(): Observable<AziendaDTO[]>{
    return this.http.get<AziendaDTO[]>(`${this.apiUrl}/elencoAziende`, {
      withCredentials:true
    });
  }

  eliminaAzienda(idAdmin: number | undefined): Observable<string> {
    console.log("elimino azienda con id", idAdmin)
    return this.http.post(`${this.apiUrl}/eliminaAzienda`, idAdmin, {
      responseType: 'text',
    withCredentials:true
    });
  }
}
