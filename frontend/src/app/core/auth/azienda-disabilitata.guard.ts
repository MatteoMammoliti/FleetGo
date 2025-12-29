import {CanActivateFn, Router} from '@angular/router';
import {inject} from '@angular/core';
import {AuthService} from '@core/auth/auth-service';

export const AziendaDisabilitataGuard: CanActivateFn = (route, state) => {

  const authService = inject(AuthService);
  const router = inject(Router);

  const idAzienda = authService.idAzienda();
  const ruolo = authService.ruoloUtenteCorrente();
  const ruoloRichiesto = route.data['ruolo'];
  const aziendaAttiva = authService.aziendaAttiva();

  if (ruolo !== ruoloRichiesto) {
    return true;
  }

  if (idAzienda && aziendaAttiva) {
    return true;
  }

  console.warn("Azienda disabilitata, accesso alla dashboard non consentito");
  return router.createUrlTree(['/azienda-disabilitata']);
};
