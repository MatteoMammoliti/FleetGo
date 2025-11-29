import {Routes} from '@angular/router';
import{LoginComponent} from '@features/login/login.component';
import {DashboardComponent} from '@features/dashboard/dashboard.component';
import {RegistrazioneComponent} from '@features/registrazione/registrazione.component';
import { Admin } from './layouts/admin/admin';

export const routes: Routes = [

  {path: 'login', component: LoginComponent},
  {path: 'dashboard', component: DashboardComponent},
  {path: 'registrazione', component: RegistrazioneComponent},
  {path: 'dashboardFleetGo', component: Admin, children: [ 
    { path: '', redirectTo: 'flotta', pathMatch: 'full' },
    /*{path: 'flotta', component: FlottaGlobale},*/ ]
  }
];
