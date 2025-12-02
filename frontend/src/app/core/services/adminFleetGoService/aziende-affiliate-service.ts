import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {AdminAziendaleDTO} from '@models/adminAziendaleDTO.models';
import {AziendaDTO} from '@models/aziendaDTO';
import {ContenitoreDatiRegistrazioneAzienda} from '@models/ContenitoreDatiRegistrazioneAzienda';

@Injectable({
  providedIn: 'root',
})

export class AziendeAffiliateService {

  constructor(private http: HttpClient) {}

  private apiUrl = 'http://localhost:8080/dashboardFleetGo';

  registraAzienda(admin: AdminAziendaleDTO, azienda: AziendaDTO){

    const contenitore:ContenitoreDatiRegistrazioneAzienda = {
      admin: admin,
      azienda: azienda
    }

    return this.http.post(`${this.apiUrl}/registraAdminAziendale`, contenitore, { responseType: 'text' });
  }
}
