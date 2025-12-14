import {Injectable} from '@angular/core';
import {environment} from '@env/environment';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {RichiestaNoleggioDTO} from '@core/models/richiestaNoleggioDTO.models';
import {StatisticheDipendenteDTO} from '@core/models/StatisticheDipendenteDTO';

@Injectable({
  providedIn:'root'
})

export class HomeService{
  private apiUrl = environment.apiUrl+'/dashboardDipendente';

  constructor(private http: HttpClient) {}

  richiediProssimoViaggio():Observable<RichiestaNoleggioDTO>{
    return this.http.get<RichiestaNoleggioDTO>(`${this.apiUrl}/prossimoViaggio`, {
      withCredentials:true
    })
  }
  richiediStatisticheDipendente():Observable<StatisticheDipendenteDTO>{
    return this.http.get<StatisticheDipendenteDTO>(`${this.apiUrl}/statisticheDipendente`, {
      withCredentials:true
    })
  }
}
