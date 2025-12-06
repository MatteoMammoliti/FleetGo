import { Routes } from '@angular/router';
import { GeneralLayoutFleetGo } from '../../layouts/general-layout-fleet-go/general-layout-fleet-go';
import { DashboardFleetGo } from './dashboard-fleet-go/dashboard-fleet-go';
import { FlottaGlobale } from './flotta-globale/flotta-globale';
import { AziendeAffiliate } from './aziende-affiliate/aziende-affiliate'
import {DettagliVeicolo} from '@shared/dettagli-veicolo/dettagli-veicolo';

export const ADMIN_FLEET_GO_ROUTES: Routes = [
  {
    path: '',
    component: GeneralLayoutFleetGo,
    children: [
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' },


      { path: 'dashboard', component: DashboardFleetGo },

      { path: 'flotta-globale', component: FlottaGlobale },

      { path: 'aziende-affiliate', component: AziendeAffiliate },
      { path: 'dettagli-veicolo/:targa', component: DettagliVeicolo }
    ]
  }
];
