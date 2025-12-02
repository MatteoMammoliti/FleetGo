import {Routes} from '@angular/router';
import{LoginComponent} from '@features/login/login.component';
import {RegistrazioneComponent} from '@features/registrazione/registrazione.component';
import {DashboardFleetGo} from '@features/adminFleetGo/dashboard-fleet-go/dashboard-fleet-go';
import {DashboardAzienda} from '@features/azienda/dashboard-azienda/dashboard-azienda';
import {DashboardDipendente} from '@features/dipendente/dashboard-dipendente/dashboard-dipendente';
import {FlottaGlobale} from '@features/adminFleetGo/flotta-globale/flotta-globale';
import {AziendeAffiliate} from '@features/adminFleetGo/aziende-affiliate/aziende-affiliate';

export const routes: Routes = [

  {path: 'login', component: LoginComponent},
  {path: 'registrazione', component: RegistrazioneComponent},
  {path:'dashboardFleetGo', component: DashboardFleetGo},
  {path:'dashboardAzienda', component: DashboardAzienda},
  {path:'dashboardDipendente', component: DashboardDipendente},
  {path:'dashboardFleetGo/flotta-globale', component:FlottaGlobale},
  {path:'dashboardFleetGo/aziende-affiliate', component: AziendeAffiliate}
]
