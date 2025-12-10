import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {ContenitoreStatisticheNumeriche} from '@models/ContenitoreStatisticheNumeriche';


@Injectable({
  providedIn : 'root'
})

export class DashboardFleetGoService{
  constructor(private http:HttpClient) {}
  private apiUrl = 'http://localhost:8080/dashboardFleetGo';

  richiediStatistiche():Observable<ContenitoreStatisticheNumeriche>{
    return this.http.get<ContenitoreStatisticheNumeriche>(`${this.apiUrl}/statistiche`, {
      withCredentials:true
  });
  }
}
