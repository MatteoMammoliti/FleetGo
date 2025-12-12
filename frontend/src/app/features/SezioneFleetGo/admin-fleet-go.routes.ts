import { Routes } from '@angular/router';
import { GeneralLayoutFleetGo } from '../../layouts/general-layout-fleet-go/general-layout-fleet-go';
import { DashboardFleetGo } from '@features/SezioneFleetGo/Pagine/dashboard-fleet-go/dashboard-fleet-go';
import { FlottaGlobale } from '@features/SezioneFleetGo/Pagine/flotta-globale/flotta-globale';
import { AziendeAffiliate } from '@features/SezioneFleetGo/Pagine/aziende-affiliate/aziende-affiliate'
import {DettagliVeicolo} from '@features/SezioneFleetGo/Pagine/dettagli-veicolo/dettagli-veicolo';
import {StoricoFatture} from '@features/SezioneFleetGo/Pagine/storico-fatture/storico-fatture';

export const ADMIN_FLEET_GO_ROUTES: Routes = [
  {
    path: '',
    component: GeneralLayoutFleetGo,
    children: [
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' },

      { path: 'dashboard', component: DashboardFleetGo },

      { path: 'flotta-globale', component: FlottaGlobale },

      { path: 'aziende-affiliate', component: AziendeAffiliate },

      { path: 'dettagli-veicolo/:targa', component: DettagliVeicolo },

      { path: 'storico-fatture', component: StoricoFatture }
    ]
  }
];
