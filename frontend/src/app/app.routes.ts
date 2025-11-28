import {Routes} from '@angular/router';
import{LoginComponent} from '@features/login/login.component';
import {DashboardComponent} from '@features/dashboard/dashboard.component';
import {RegistrazioneComponent} from '@features/registrazione/registrazione.component';

export const routes: Routes = [

  {path: 'login', component: LoginComponent},
  {path: 'dashboard', component: DashboardComponent},
  {path: 'registrazione', component: RegistrazioneComponent},

];
