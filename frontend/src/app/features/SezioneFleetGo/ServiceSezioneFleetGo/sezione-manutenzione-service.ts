import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '@env/environment';
import {Observable} from 'rxjs';
import {ContenitoreStatisticheNumericheManutezioni} from '@core/models/ContenitoreStatisticheNumericheManutezioni';
import {RichiestaManutenzioneDTO} from '@core/models/RichiestaManutenzioneDTO';


@Injectable({
  providedIn:"root"
})

export class SezioneManutenzioneService{
  constructor(private http:HttpClient) {}

  private apiUrl= environment.apiUrl+'/dashboardFleetGo';

  public prelevaDatiManutenzione():Observable<ContenitoreStatisticheNumericheManutezioni>{
    return this.http.get<ContenitoreStatisticheNumericheManutezioni>(`${this.apiUrl}/datiManutenzioni`,{withCredentials:true});
  }

  public prelevaManutezioniInCorso():Observable<RichiestaManutenzioneDTO[]>{
    return this.http.get<RichiestaManutenzioneDTO[]>(`${this.apiUrl}/manutenzioniInCorso`,{withCredentials:true});
  }

  public prelevaManutenzioniStorico():Observable<RichiestaManutenzioneDTO[]>{
    return this.http.get<RichiestaManutenzioneDTO[]>(`${this.apiUrl}/manutenzioniStorico`,{withCredentials:true})
  }

  public chiudiRichiestaManutenzione(idRichiesta:number):Observable<string>{
    return this.http.post(`${this.apiUrl}/chiudiRichiestaManutenzione${idRichiesta}`,{},{
      responseType:'text',
      withCredentials:true})
  }
}
