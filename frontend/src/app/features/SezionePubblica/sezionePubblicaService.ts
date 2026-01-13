import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../../../environment';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})

export class SezionePubblicaService {
  constructor(private http: HttpClient) {}

  private apiUrl = environment.apiUrl + '/sezionePubblica';

  inviaForm(form: FormData): Observable<string> {
    return this.http.post(`${this.apiUrl}/formContatto`, form, {withCredentials: true, responseType: 'text'})
  }
}
