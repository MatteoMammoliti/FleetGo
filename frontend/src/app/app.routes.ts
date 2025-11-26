import {Routes} from '@angular/router';
import{Login} from '@features/login/login';
import {Dashboard} from '@features/dashboard/dashboard';
import {Registration} from '@features/registration/registration';

export const routes: Routes = [

  {path: 'login',
    component: Login},
  {path: 'dashboard',
    component: Dashboard},
  {path: 'registration',
  component: Registration},

];
