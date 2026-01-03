import { Routes } from '@angular/router';
import {DashboardAziendaDisabilitata} from '@features/SezioneAdminAziendaDisabilitata/dashboard-azienda-disabilitata/dashboard-azienda-disabilitata';
import {HomeDipendenteSenzaAzienda} from '@features/DipendenteSenzaAzienda/home-dipendente-senza-azienda/home-dipendente-senza-azienda';
import {
  GeneralLayoutDipendenteSenzaAzienda
} from '../../layouts/general-layout-dipendente-senza-azienda/general-layout-dipendente-senza-azienda';

export const DIPENDENTE_SENZA_AZIENDA: Routes = [
  {
    path: '',
    component: GeneralLayoutDipendenteSenzaAzienda,
    children: [
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
      { path: 'dashboard', component: HomeDipendenteSenzaAzienda },
    ]
  }
];
