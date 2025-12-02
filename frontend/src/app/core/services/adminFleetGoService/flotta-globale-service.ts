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
  private apiUrl= 'http://localhost:8080/fleetGoDashboard';

  registraVeicolo(formData: FormData)   {

    return this.http.post(`${this.apiUrl}/aggiuntaVeicoli`, formData, { responseType: 'text' });
  }

  richiediVeicoli():Observable<VeicoloDTO[]>{
    return this.http.get<VeicoloDTO[]>(`${this.apiUrl}/listaVeicoli`);
  }

}
