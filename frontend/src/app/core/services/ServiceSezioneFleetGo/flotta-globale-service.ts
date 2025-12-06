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

  registraVeicolo(formData: FormData):Observable<VeicoloDTO>   {
    return this.http.post<VeicoloDTO>(`${this.apiUrl}/registraVeicolo`, formData, { withCredentials:true});
  }

  richiediVeicoli():Observable<VeicoloDTO[]>{
    return this.http.get<VeicoloDTO[]>(`${this.apiUrl}/listaVeicoli`, {
      withCredentials:true
    });
  }

  rimuoviVeicolo(targaVeicolo:string): Observable<string> {
    return this.http.post(`${this.apiUrl}/eliminaVeicolo`, targaVeicolo, {responseType: "text", withCredentials:true});
  }
}
