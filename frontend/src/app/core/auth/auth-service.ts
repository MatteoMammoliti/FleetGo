import {HttpClient, HttpParams} from '@angular/common/http';
import {DipendenteDTO} from '@core/models/dipendenteDTO.models';
import {Observable} from 'rxjs';
import {Injectable, signal} from '@angular/core';
import {ContenitoreDatiModificaPasswordDTO} from '@core/models/ContenitoreDatiModificaPasswordDTO';
import {environment} from '@env/environment';

export interface LoginResponse {
  redirectUrl: string;
  ruolo: string;
  idAzienda?: number | null;
  primoAccesso: boolean;
  isAziendaAttiva: boolean | null;

  [key: string]: any;
}

@Injectable({
  providedIn: 'root',
})

export class AuthService {
  ruoloUtenteCorrente = signal<string | null>(null);
  idAzienda = signal<number | null>(null);
  primoAccesso = signal<boolean | null>(null);
  aziendaAttiva = signal<boolean | null>(null);

  constructor(private http: HttpClient) {

    const ruoloLocale = localStorage.getItem('ruoloUtenteCorrente');
    if (ruoloLocale) {
      this.ruoloUtenteCorrente.set(ruoloLocale);
    }

    const idAziendaLocale = localStorage.getItem('idAziendaAffiliata');
    if (idAziendaLocale) {
      this.idAzienda.set(Number(idAziendaLocale));
    }

    const primoAccessoLocale = localStorage.getItem('primoAccesso');
    if (primoAccessoLocale) {
      this.primoAccesso.set(primoAccessoLocale === 'true');
    }

    const isAziendaAttiva = localStorage.getItem('isAziendaAttiva');
    if (isAziendaAttiva) {
      this.aziendaAttiva.set(isAziendaAttiva === 'true');
    }
  }

  apiUrl = environment.apiUrl + '/autenticazione';

  registrazione(utente: DipendenteDTO, immaginePatente: File): Observable<string> {
    const formData = new FormData();
    formData.append("immagine", immaginePatente);
    formData.append("utente", new Blob([JSON.stringify(utente)], {type: 'application/json'}));
    return this.http.post(`${this.apiUrl}/registrazione`, formData, {responseType: 'text'});
  }

  login(email: string, password: string): Observable<LoginResponse> {

    const body = new HttpParams()
      .set('email', email)
      .set('password', password);

    return this.http.post<LoginResponse>(`${this.apiUrl}/login`, body, {
      headers: {'Content-Type': 'application/x-www-form-urlencoded'},
      withCredentials: true
    });
  }

  salvaDatiLogin(response: any) {

    this.ruoloUtenteCorrente.set(response.ruolo);
    this.idAzienda.set(response.idAzienda || null);
    this.primoAccesso.set(response.primoAccesso);
    this.aziendaAttiva.set(response.isAziendaAttiva);

    localStorage.setItem('ruoloUtenteCorrente', response.ruolo);

    if (response.idAzienda) {
      localStorage.setItem('idAziendaAffiliata', response.idAzienda.toString());
    } else {
      localStorage.removeItem('idAziendaAffiliata');
    }

    localStorage.setItem('primoAccesso', String(response.primoAccesso));
    localStorage.setItem('isAziendaAttiva', String(response.isAziendaAttiva));
    localStorage.setItem('utente', JSON.stringify(response));
  }

  logout() {

    this.primoAccesso.set(null);
    this.ruoloUtenteCorrente.set(null);
    this.idAzienda.set(null);
    this.aziendaAttiva.set(null);

    localStorage.removeItem('idAziendaAffiliata');
    localStorage.removeItem('ruoloUtenteCorrente');
    localStorage.removeItem('primoAccesso');
    localStorage.removeItem('isAziendaAttiva');
    localStorage.removeItem('utente');
    localStorage.removeItem('appuntamento_inviato');

    return this.http.post(`${this.apiUrl}/logout`, {}, {responseType: 'text', withCredentials: true});
  }

  invioOTP(email: string) {

    const formData = new FormData();
    formData.append("email", email);

    return this.http.post(`${this.apiUrl}/richiediCodiceOTP`, formData, {responseType: 'text'});
  }

  cambioPassword(email: string, otp: string, nuovaPassword: string): Observable<string> {

    const dati: ContenitoreDatiModificaPasswordDTO = {
      email: email,
      nuovaPassword: nuovaPassword,
      codiceOTP: otp
    }

    return this.http.post(`${this.apiUrl}/modificaPassword`, dati, {responseType: 'text'});
  }
}
