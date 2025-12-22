import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from './auth-service';

export const checkAziendaGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  const idAzienda = authService.idAzienda();
  const ruolo = authService.ruoloUtenteCorrente();

  if (ruolo !== 'Dipendente') {
    return true;
  }

  if (idAzienda) {
    return true;
  }
  console.warn("Dipendente senza azienda: redirect all'onboarding");
  return router.createUrlTree(['/senza-azienda']);
};
