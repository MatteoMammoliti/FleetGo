import {HttpClient, HttpParams} from '@angular/common/http';
import {DipendenteDTO} from '@core/models/dipendenteDTO.models';
import {Observable} from 'rxjs';
import { Injectable, signal } from '@angular/core';
import {ContenitoreDatiModificaPasswordDTO} from '@core/models/ContenitoreDatiModificaPasswordDTO';
import {environment} from '@env/environment';

export interface LoginResponse {
  redirectUrl: string;
  ruolo: string;
  idAzienda?:number | null;
}

@Injectable({
  providedIn: 'root',
})

export class AuthService {
  ruoloUtenteCorrente = signal<string | null>(null);
  idAzienda=signal<number | null>(null);

  constructor (private http: HttpClient) {

    const ruoloLocale=localStorage.getItem('ruoloUtenteCorrente');
    if (ruoloLocale){
      this.ruoloUtenteCorrente.set(ruoloLocale);
    }
  }
  apiUrl = environment.apiUrl+'/autenticazione';


  registrazione(utente:DipendenteDTO,immaginePatente:File ):Observable<string> {
    const formData = new FormData();
    formData.append("immagine",immaginePatente);
    formData.append("utente", new Blob([JSON.stringify(utente)], { type: 'application/json' }));
    return this.http.post(`${this.apiUrl}/registrazione`, formData, { responseType: 'text' });
  }

  login(email:string, password:string) :Observable<LoginResponse>{

    const body = new HttpParams()
      .set('email', email)
      .set('password', password);

    return this.http.post<LoginResponse>(`${this.apiUrl}/login`, body, {
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
      withCredentials:true
    });
  }

  aggiornaRuoloUtenteCorrente(ruoloRicevuto: string,idAzienda: number | null) {
    console.log("Aggiorno utente corrente in auth-service.ts con ruolo: " + ruoloRicevuto);
    this.ruoloUtenteCorrente.set(ruoloRicevuto);
    localStorage.setItem('ruoloUtenteCorrente', ruoloRicevuto);
    if (idAzienda) {
      this.idAzienda.set(idAzienda);
      localStorage.setItem('idAziendaAffiliata', idAzienda.toString());
    } else {
      this.idAzienda.set(null);
      localStorage.removeItem('idAziendaAffiliata');
    }
  }

  logout(){
    this.ruoloUtenteCorrente.set(null);
    this.idAzienda.set(null);
    localStorage.removeItem('idAziendaAffiliata');
    localStorage.removeItem('ruoloUtenteCorrente');
    return this.http.post(`${this.apiUrl}/logout`, {}, { responseType: 'text', withCredentials: true });
  }

  invioOTP(email: string) {

    const formData = new FormData();
    formData.append("email", email);

    return this.http.post(`${this.apiUrl}/richiediCodiceOTP`, formData, { responseType: 'text' });
  }

  cambioPassword(email: string, otp: string, nuovaPassword: string): Observable<string> {

    const dati: ContenitoreDatiModificaPasswordDTO = {
      email: email,
      nuovaPassword: nuovaPassword,
      codiceOTP: otp
    }

    return this.http.post(`${this.apiUrl}/modificaPassword`, dati, { responseType: 'text' });
  }
}
