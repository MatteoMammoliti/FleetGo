import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {DipendenteDTO} from '@models/dipendenteDTO.models';

@Injectable({
  providedIn: 'root',
})

export class DipendentiService {
  private apiUrl = 'http://localhost:8080/dashboardAdminAziendale';

  constructor(private http: HttpClient) {}

  public getDipendenti(): Observable<DipendenteDTO[]> {
    return this.http.get<DipendenteDTO[]>(`${this.apiUrl}/getDipendenti`, { withCredentials: true });
  }

  public rimuoviDipendente(idUtente: number | undefined): Observable<any> {
    return this.http.post(`${this.apiUrl}/rimuoviDipendente`, idUtente, { responseType: 'text', withCredentials: true});
  }
  public getDipendenteID(id: string): Observable<DipendenteDTO> {
    return this.http.get<DipendenteDTO>(`${this.apiUrl}/getDipendente/${id}`, { withCredentials: true });
  }
  public aggiornaDipendente(dipendente: DipendenteDTO): Observable<any> {
    return this.http.post(`${this.apiUrl}/aggiornaDipendente`, dipendente, { responseType: 'text', withCredentials: true });
  }
}
