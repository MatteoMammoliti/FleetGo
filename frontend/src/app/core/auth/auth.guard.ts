import {inject} from '@angular/core';
import {CanActivateFn, Router} from '@angular/router';
import {AuthService} from './auth-service';

export const authGuard: CanActivateFn = (route, state) => {

  const authService = inject(AuthService);
  const router = inject(Router);

  const RuoloUtente = authService.ruoloUtenteCorrente();
  const RuoloRichiesto = route.data['ruolo'];

  if ((RuoloUtente) && (!RuoloRichiesto || RuoloRichiesto == RuoloUtente)) {
    return true;
  }

  console.warn("Accesso pagina negato, ruolo non autorizzato");
  authService.logout();
  router.navigate(['login']);
  return false;
}
