import {HttpClient} from '@angular/common/http';
import {DipendenteDTO} from '@models/dipendenteDTO.models';
import {Observable} from 'rxjs';
import { Injectable, signal } from '@angular/core';

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

  login(email:string, password:string) :Observable<string>{
    const formData = new FormData();
    formData.append("email", email);
    formData.append("password", password);

    return this.http.post(`${this.apiUrl}/login`, formData, { responseType: 'text' });
  }

  aggiornaRuoloUtenteCorrente(ruoloRicevuto: string) {
    console.log("Aggiorno utente corrente in auth-service.ts con ruolo: " + ruoloRicevuto);
    this.ruoloUtenteCorrente.set(ruoloRicevuto);
    localStorage.setItem('ruoloUtenteCorrente', ruoloRicevuto);
  }

  logout(){
    this.ruoloUtenteCorrente.set(null);
    localStorage.removeItem('ruoloUtenteCorrente');
  }
}
