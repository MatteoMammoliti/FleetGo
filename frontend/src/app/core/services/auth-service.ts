import {HttpClient, HttpParams} from '@angular/common/http';
import {DipendenteDTO} from '@models/dipendenteDTO.models';
import {Observable} from 'rxjs';
import { Injectable, signal } from '@angular/core';
import {ContenitoreDatiModificaPasswordDTO} from '@models/ContenitoreDatiModificaPasswordDTO';

export interface LoginResponse {
  redirectUrl: string;
  ruolo: string;
}

@Injectable({
  providedIn: 'root',
})

export class AuthService {
  ruoloUtenteCorrente = signal<string | null>(null);

  constructor (private http: HttpClient) {

    const ruoloLocale=localStorage.getItem('ruoloUtenteCorrente');
    if (ruoloLocale){
      this.ruoloUtenteCorrente.set(ruoloLocale);
    }
  }
  apiUrl = 'http://localhost:8080/autenticazione';


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

  aggiornaRuoloUtenteCorrente(ruoloRicevuto: string) {
    console.log("Aggiorno utente corrente in auth-service.ts con ruolo: " + ruoloRicevuto);
    this.ruoloUtenteCorrente.set(ruoloRicevuto);
    localStorage.setItem('ruoloUtenteCorrente', ruoloRicevuto);
  }

  logout(){
    this.ruoloUtenteCorrente.set(null);
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
