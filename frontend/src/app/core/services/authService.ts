import {inject, Injectable, signal} from '@angular/core';
import {CookieService} from 'ngx-cookie-service';
import {user} from '@models/user.model';
import {of, throwError} from 'rxjs';


@Injectable({
  providedIn: 'root',
})
export class AuthService {
  constructor() {
    if(this.cookieService.check('fleetgo_token')){
      if(this.cookieService.check('fleetgo_user')){
        const user = this.cookieService.get('fleetgo_user');
        this.currentUser.set(JSON.parse(user));
      }
    }
  }

  login(email: string, password: string){
    if(email==="1@1"&&password==="2"){
      const userLoggato : user = {
        email:email,
        nome:'cristian',
        ruolo:'administrator'
      };
      const token = "123456789"
      this.currentUser.set(userLoggato);
      this.cookieService.set('fleetgo_token', token);
      this.cookieService.set('fleetgo_user', JSON.stringify(userLoggato));
      return of(true);
    }
    return throwError(() => new Error('Credenziali errate'));
  }

  private cookieService=inject(CookieService);
  currentUser=signal<any> (null);
}
