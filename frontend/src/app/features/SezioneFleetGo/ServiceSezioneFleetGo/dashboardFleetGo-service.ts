import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {ContenitoreStatisticheNumeriche} from '@core/models/ContenitoreStatisticheNumeriche';
import {environment} from '@env/environment';
import {FatturaDaGenerareDTO} from '@core/models/FatturaDaGenerareDTO';
import {
  RichiesteManutenzioneDaGestire
} from '@features/SezioneFleetGo/Componenti/richieste-manutenzione-da-gestire/richieste-manutenzione-da-gestire';
import {RichiestaManutenzioneDTO} from '@core/models/RichiestaManutenzioneDTO';


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

  richiediFattureDaGenerare():Observable<FatturaDaGenerareDTO[]>{
    return this.http.get<FatturaDaGenerareDTO[]>(`${this.apiUrl}/fattureDaGenerare`,{
      withCredentials:true
    });
  }
  richiediManutenzioneDaGestire():Observable<RichiestaManutenzioneDTO[]>{
    return this.http.get<RichiestaManutenzioneDTO[]>(`${this.apiUrl}/richiesteManutezioneDaAccettare`,{
      withCredentials:true
    });
  }

  richiediInformazioniSuManutenzioneDaGestire(idManutenzione:number):Observable<RichiestaManutenzioneDTO>{
    return this.http.get<RichiestaManutenzioneDTO>(`${this.apiUrl}/dettagliRichiestaManutenzone/${idManutenzione}`,{withCredentials:true});
  }
}
