import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {ContenitoreStatisticheNumeriche} from '@core/models/ContenitoreStatisticheNumeriche';
import {environment} from '@env/environment';


@Injectable({
  providedIn : 'root'
})

export class DashboardFleetGoService{
  constructor(private http:HttpClient) {}
  private apiUrl = environment.apiUrl+'/dashboardFleetGo';

  richiediStatistiche():Observable<ContenitoreStatisticheNumeriche>{
    return this.http.get<ContenitoreStatisticheNumeriche>(`${this.apiUrl}/statistiche`, {
      withCredentials:true
  });
  }
}
