import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {ContenitoreStatisticheNumericheFleetGo} from '@models/ContenitoreStatisticheNumericheFleetGo';


@Injectable({
  providedIn : 'root'
})

export class DashboardFleetGoService{
  constructor(private http:HttpClient) {}
  private apiUrl = 'http://localhost:8080/dashboardFleetGo';

  richiediStatistiche():Observable<ContenitoreStatisticheNumericheFleetGo>{
    return this.http.get<ContenitoreStatisticheNumericheFleetGo>(`${this.apiUrl}/statistiche`, {
      withCredentials:true
  });
  }
}
