import { Routes } from '@angular/router';
import {GeneralLayoutAziendaDisabilitata} from '../../layouts/general-layout-azienda-disabilitata/general-layout-azienda-disabilitata';
import {DashboardAziendaDisabilitata} from '@features/SezioneAdminAziendaDisabilitata/dashboard-azienda-disabilitata/dashboard-azienda-disabilitata';
import {StoricoFattureAdminAziendale} from '@features/SezioneAdminAziendale/Pagine/storico-fatture/storico-fatture-admin-aziendale';

export const AZIENDA_DISABILITATA_ROUTES: Routes = [
  {
    path: '',
    component: GeneralLayoutAziendaDisabilitata,
    children: [
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' },

      { path: 'dashboard', component: DashboardAziendaDisabilitata },
      { path: 'storico-fatture', component: StoricoFattureAdminAziendale}
    ]
  }
];
