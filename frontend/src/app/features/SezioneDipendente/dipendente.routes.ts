import {Routes} from '@angular/router';
import {GeneralLayoutFleetGo} from '../../layouts/general-layout-fleet-go/general-layout-fleet-go';
import {DashboardFleetGo} from '@features/SezioneFleetGo/dashboard-fleet-go/dashboard-fleet-go';
import {DashboardDipendente} from '@features/SezioneDipendente/dashboard-dipendente/dashboard-dipendente';

export const DIPENDENTE_ROUTES: Routes = [
  {
    path: '',
    component: GeneralLayoutFleetGo,
    children: [
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
      { path: 'dashboard', component: DashboardDipendente },
    ]
  }]
