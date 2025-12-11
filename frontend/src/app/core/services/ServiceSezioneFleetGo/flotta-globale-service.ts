import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {VeicoloDTO} from '@models/veicoloDTO.model';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class FlottaGlobaleService {
  constructor(private http: HttpClient) {
  }
  private apiUrl= 'http://localhost:8080/dashboardFleetGo';

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
    return this.http.post(`${this.apiUrl}/modificaVeicolo`, veicolo, {responseType: "text", withCredentials:true});
  }
}
