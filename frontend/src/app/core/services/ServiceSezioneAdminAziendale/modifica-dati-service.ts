import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {ContenitoreDatiRegistrazioneAzienda} from '@models/ContenitoreDatiRegistrazioneAzienda';
import {Observable} from 'rxjs';
import {ModificaDatiUtenteDTO} from '@models/ModificaDatiUtenteDTO';

@Injectable({
  providedIn: 'root',
})

export class ModificaDatiService {

  constructor(private http: HttpClient) {}

  private apiUrl = 'http://localhost:8080/dashboardAdminAziendale';

  public modificaDati(): Observable<string> {

    const contenitore: ModificaDatiUtenteDTO = {
      nome: '',
      cognome: '',
      email: '',
      password: ''
    }

    return this.http.post(`${this.apiUrl}/modificaDati`, contenitore, { responseType: 'text'});
  }
}
