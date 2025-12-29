import { Routes } from '@angular/router';
import { GeneralLayoutAdminAziendale } from '../../layouts/general-layout-admin-aziendale/general-layout-admin-aziendale';
import { DashboardAzienda } from '@features/SezioneAdminAziendale/Pagine/dashboard-admin-aziendale/dashboard-azienda';
import { ModificaDatiComponent } from '@features/SezioneAdminAziendale/Pagine/modifica-dati/modifica-dati';
import { GestioneDipendentiComponent} from '@features/SezioneAdminAziendale/Pagine/gestione-dipendenti/gestione-dipendenti';
import {FlottaAdminAziendale} from '@features/SezioneAdminAziendale/Pagine/flotta-admin-aziendale/flotta-admin-aziendale';
import {DettagliVeicoloAziendale} from '@features/SezioneAdminAziendale/Pagine/dettagli-veicolo-aziendale/dettagli-veicolo-aziendale';
import {Prenotazioni} from '@features/SezioneAdminAziendale/Pagine/prenotazioni/prenotazioni';
import {StoricoFattureAdminAziendale} from '@features/SezioneAdminAziendale/Pagine/storico-fatture/storico-fatture-admin-aziendale';
import {CheckSedeImpostataGuard} from '@core/auth/check-sede-impostata.guard';

export const AZIENDA_ROUTES: Routes = [
  {
    path: '',
    component: GeneralLayoutAdminAziendale,
    canActivateChild: [CheckSedeImpostataGuard],
    children: [
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
      { path: 'dashboard', component: DashboardAzienda },
      { path: 'impostazioni', component: ModificaDatiComponent},
      { path: 'dipendenti', component : GestioneDipendentiComponent},
      { path: 'flotta', component : FlottaAdminAziendale },
      { path: 'dettagli-veicolo/:targa', component: DettagliVeicoloAziendale},
      { path: 'prenotazioni', component: Prenotazioni},
      { path: 'storico-fatture', component: StoricoFattureAdminAziendale }
    ],
  },
];
