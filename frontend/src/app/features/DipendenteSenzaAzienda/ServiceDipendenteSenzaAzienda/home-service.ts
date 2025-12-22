import {Injectable} from '@angular/core';
import {environment} from '@env/environment';
import {HttpClient} from '@angular/common/http';
import {ContenitoreDatiAzienda} from '@core/models/ContenitoreDatiAzienda';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root',
})

export class HomeService{
  private apiUrl = environment.apiUrl+'/homeNoAzienda';

  constructor(private http: HttpClient) {}

  getAziende(): Observable<ContenitoreDatiAzienda[]> {
    return this.http.get<ContenitoreDatiAzienda[]>(`${this.apiUrl}/getAziende`, { withCredentials:true })
  }

  getRichiestaAffiliazioneAttesa():Observable<ContenitoreDatiAzienda | null> {
    return this.http.get<ContenitoreDatiAzienda | null>(`${this.apiUrl}/getRichiestaAttesa`, { withCredentials:true })
  }

  annullaRichiestaInAttesa(azienda:number):Observable<any>{
    return this.http.post(`${this.apiUrl}/annullaRichiestaAttesa${azienda}`,{}, {
      responseType: 'text',
      withCredentials:true })
  }

  inviaRichiestaAffiliazione(azienda:number):Observable<any>{
    return this.http.post(`${this.apiUrl}/inviaRichiestaAffiliazione${azienda}`,{}, {
      responseType: 'text',
      withCredentials:true })
  }

}
