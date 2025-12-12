import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {VeicoloDTO} from '@core/models/veicoloDTO.model';
import {Observable} from 'rxjs';
import {environment} from '@env/environment';

@Injectable({
  providedIn: 'root',
})
export class FlottaGlobaleService {
  constructor(private http: HttpClient) {
  }
  private apiUrl= environment.apiUrl+'/dashboardFleetGo';

  registraVeicolo(formData: FormData):Observable<string>   {
    return this.http.post(`${this.apiUrl}/registraVeicolo`, formData, {
      responseType: 'text',
      withCredentials: true
    });
  }

  richiediVeicoli():Observable<VeicoloDTO[]>{
    return this.http.get<VeicoloDTO[]>(`${this.apiUrl}/listaVeicoli`, {
      withCredentials:true
    });
  }

  rimuoviVeicolo(targaVeicolo:string): Observable<string> {
    return this.http.post(`${this.apiUrl}/eliminaVeicolo`, targaVeicolo, {responseType: "text", withCredentials:true});
  }

  richiediVeicolo(targa:string|null):Observable<VeicoloDTO>{
    return this.http.get<VeicoloDTO>(`${this.apiUrl}/informazioneVeicolo/${targa}`, {withCredentials:true});
  }

  inviaModifiche(veicolo:VeicoloDTO):Observable<string>{
    console.log("sono qua")
    console.log("invio", veicolo)
    return this.http.post(`${this.apiUrl}/modificaVeicolo`, veicolo, {responseType: "text", withCredentials:true});
  }
}
