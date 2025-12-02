import { Routes } from '@angular/router';
import { LoginComponent } from '@features/login/login.component';
import { RegistrazioneComponent } from '@features/registrazione/registrazione.component';
import { GeneralLayoutNoLogin } from './layouts/general-layout-no-login/general-layout-no-login';

export const routes: Routes = [
 {
    path: '',
    component: GeneralLayoutNoLogin,
    children: [
      { path: '', redirectTo: 'login', pathMatch: 'full' },
      { path: 'login', component: LoginComponent },
      { path: 'registrazione', component: RegistrazioneComponent }
    ]
  },

  {
    path: 'dashboardFleetGo',
    loadChildren: () => import('./features/adminFleetGo/admin-fleet-go.routes')
      .then(m => m.ADMIN_FLEET_GO_ROUTES)
  },


  { path: '**', redirectTo: 'login' }
];
