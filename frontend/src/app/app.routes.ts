import {Routes} from '@angular/router';
import { Login } from '@features/login/login'
import { Dashboard } from '@features/dashboard/dashboard';
import { Registrazione } from '@features/registrazione/registrazione'

export const routes: Routes = [

  {path: 'login', component: Login},
  {path: 'dashboard', component: Dashboard},
  {path: 'registrazione', component: Registrazione}

];
