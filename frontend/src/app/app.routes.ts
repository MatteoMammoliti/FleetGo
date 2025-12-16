import { Routes } from '@angular/router';
import { LoginComponent } from '@features/SezioneAutenticazione/Pagine/login/login.component';
import { RegistrazioneComponent } from '@features/SezioneAutenticazione/Pagine/registrazione/registrazione.component';
import { GeneralLayoutNoLogin } from './layouts/general-layout-no-login/general-layout-no-login';
import {RecuperoPassword} from '@features/SezioneAutenticazione/Pagine/recupero-password/recupero-password';
import {authGuard} from '@core/auth/auth.guard';
import {LandingPage} from '@features/SezionePubblica/landing-page/landing-page';

export const routes: Routes = [
 {
    path: '',
    component: GeneralLayoutNoLogin,
    children: [
      { path: '', component: LandingPage },
      { path: 'login', component: LoginComponent },
      { path: 'registrazione', component: RegistrazioneComponent },
      { path: 'recuperoPassword', component: RecuperoPassword}
    ]
  },

  {
    path: 'dashboardFleetGo',
    loadChildren: () => import('@features/SezioneFleetGo/admin-fleet-go.routes')
      .then(m => m.ADMIN_FLEET_GO_ROUTES),
    canActivate: [authGuard],
    data: { ruolo: 'FleetGo'}
  },

  {
    path: 'dashboardAzienda',
    loadChildren: () => import('@features/SezioneAdminAziendale/admin-aziendale.routes')
      .then(m => m.AZIENDA_ROUTES),
    canActivate: [authGuard],
    data: { ruolo: 'AdminAziendale' }
  },

  {
    path: 'dashboardDipendente',
    loadChildren: () => import('@features/SezioneDipendente/dipendente.routes')
      .then(m => m.DIPENDENTE_ROUTES),
    canActivate: [authGuard],
    data: { ruolo: 'Dipendente' }
  },

  { path: '**', redirectTo: '' }
];
