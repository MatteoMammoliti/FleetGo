import {Injectable} from '@angular/core';
import {environment} from '@env/environment';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {ModificaDatiUtenteDTO} from '@core/models/ModificaDatiUtenteDTO';

@Injectable({
  providedIn:'root'
})

export class ImpostazioniService{
  private apiUrl = environment.apiUrl+'/dashboardDipendente';

  constructor(private http: HttpClient) {}

  public getDipendente():Observable<ModificaDatiUtenteDTO>{
    return this.http.get<ModificaDatiUtenteDTO>(`${this.apiUrl}/dettagliUtente`, {
      withCredentials:true
    })
  }

  public inviaModifiche(dati:ModificaDatiUtenteDTO):Observable<any>{
    return this.http.post(`${this.apiUrl}/applicaModifiche`, dati,{
      withCredentials:true
    })
  }
}
