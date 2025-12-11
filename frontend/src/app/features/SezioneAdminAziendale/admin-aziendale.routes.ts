import { Routes } from '@angular/router';
import { GeneralLayoutAdminAziendale } from '../../layouts/app-general-layout-admin-aziendale/general-layout-admin-aziendale';
import { DashboardAzienda } from '@features/SezioneAdminAziendale/Pagine/dashboard-admin-aziendale/dashboard-azienda';
import { ModificaDatiComponent } from '@features/SezioneAdminAziendale/Pagine/modifica-dati/modifica-dati';
import { GestioneDipendentiComponent} from '@features/SezioneAdminAziendale/Pagine/gestione-dipendenti/gestione-dipendenti';
import { DettagliDipendente } from '@features/SezioneAdminAziendale/Componenti/dettagli-dipendente/dettagli-dipendente';
import {FlottaAdminAziendale} from '@features/SezioneAdminAziendale/Pagine/flotta-admin-aziendale/flotta-admin-aziendale';


export const AZIENDA_ROUTES: Routes = [
  {
    path: '',
    component: GeneralLayoutAdminAziendale,
    children: [
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' },

      { path: 'dashboard', component: DashboardAzienda },

      { path: 'impostazioni', component: ModificaDatiComponent },
      { path: 'dipendenti', component : GestioneDipendentiComponent},

      { path: 'dettagli-dipendente/:id', component : DettagliDipendente },
      { path: 'flotta', component : FlottaAdminAziendale }
    ],
  },
];
