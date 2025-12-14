import {Routes} from '@angular/router';
import {GeneralLayoutFleetGo} from '../../layouts/general-layout-fleet-go/general-layout-fleet-go';
import {DashboardFleetGo} from '@features/SezioneFleetGo/Pagine/dashboard-fleet-go/dashboard-fleet-go';
import {DashboardDipendente} from '@features/SezioneDipendente/Pagine/dashboard-dipendente/dashboard-dipendente';
import {GeneralLayoutDipendente} from '../../layouts/general-layout-dipendente/general-layout-dipendente';

export const DIPENDENTE_ROUTES: Routes = [
  {
    path: '',
    component: GeneralLayoutDipendente,
    children: [
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
      { path: 'dashboard', component: DashboardDipendente },
    ]
  }]
