import {inject} from '@angular/core';
import {CanActivateFn, Router} from '@angular/router';
import {AuthService} from './auth-service';

export const PrimoAccessoGuard: CanActivateFn = (route, state) => {

  const authService = inject(AuthService);
  const router = inject(Router);

  const ruoloUtente = authService.ruoloUtenteCorrente();
  const ruoloRichiesto = route.data['ruolo'];
  const primoAccesso = authService.primoAccesso();

  if (ruoloUtente !== ruoloRichiesto) {
    return true;
  }

  if (!primoAccesso) {
    return true;
  }

  console.warn("Accesso pagina negato per password non modificata");
  return router.createUrlTree(['/recuperoPassword']);
}
