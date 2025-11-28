import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {DipendenteDTO} from '@models/dipendenteDTO.models';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root',
})

export class AuthService {
  constructor (private http: HttpClient) {}
  apiUrl = 'http://localhost:8080/autenticazione';

  registrazione(utente:DipendenteDTO,immaginePatente:File ):Observable<any> {
    const formData = new FormData();
    formData.append("immagine",immaginePatente);
    formData.append("utente", new Blob([JSON.stringify(utente)], { type: 'application/json' }));
    return this.http.post(`${this.apiUrl}/registrazione`, formData);
  }
}
