import {ActivatedRouteSnapshot, CanActivateFn, Router, RouterStateSnapshot, UrlTree} from '@angular/router';
import {inject} from '@angular/core';
import {DashboardService} from '@features/SezioneAdminAziendale/ServiceSezioneAdminAziendale/dashboard-service';
import {catchError, map, Observable, of} from 'rxjs';

export const CheckSedeImpostataGuard: CanActivateFn = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean | UrlTree> => {

  const dashboardService = inject(DashboardService);
  const router = inject(Router)

  return dashboardService.isSedeImpostata().pipe(
    map((isImpostata: boolean) => {

      if (isImpostata) {
        return true;
      }

      if (state.url.includes('/dashboardAzienda/dashboard')) {
        return true;
      }

      return router.createUrlTree(['/dashboardAzienda/dashboard']);
    }),

    catchError((err) => {
      console.error(err);
      return of(router.createUrlTree(['/login']));
    })
  );
}
