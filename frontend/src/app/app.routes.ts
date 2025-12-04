import { Routes } from '@angular/router';
import { LoginComponent } from '@features/login/login.component';
import { RegistrazioneComponent } from '@features/registrazione/registrazione.component';
import { GeneralLayoutNoLogin } from './layouts/general-layout-no-login/general-layout-no-login';
import {RecuperoPassword} from '@features/recupero-password/recupero-password';

export const routes: Routes = [
 {
    path: '',
    component: GeneralLayoutNoLogin,
    children: [
      { path: '', redirectTo: 'login', pathMatch: 'full' },
      { path: 'login', component: LoginComponent },
      { path: 'registrazione', component: RegistrazioneComponent },
      {path: 'recuperoPassword', component: RecuperoPassword}
    ]
  },

  {
    path: 'dashboardFleetGo',
    loadChildren: () => import('@features/SezioneFleetGo/admin-fleet-go.routes')
      .then(m => m.ADMIN_FLEET_GO_ROUTES)
  },

  {
    path: 'dashboardAzienda',
    loadChildren: () => import('@features/SezioneAdminAziendale/admin-aziendale.routes')
      .then(m => m.AZIENDA_ROUTES)
  },

  { path: '**', redirectTo: 'login' }
];
