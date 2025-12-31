import {Routes} from '@angular/router';
import {GeneralLayoutFleetGo} from '../../layouts/general-layout-fleet-go/general-layout-fleet-go';
import {DashboardFleetGo} from '@features/SezioneFleetGo/Pagine/dashboard-fleet-go/dashboard-fleet-go';
import {DashboardDipendente} from '@features/SezioneDipendente/Pagine/dashboard-dipendente/dashboard-dipendente';
import {GeneralLayoutDipendenteSenzaAzienda} from '../../layouts/general-layout-dipendente/general-layout-dipendente-senza-azienda';
import {CreaPrenotazione} from '@features/SezioneDipendente/Pagine/crea-prenotazione/crea-prenotazione';
import {
  PrenotazioniDipendente
} from '@features/SezioneDipendente/Pagine/prenotazioni-dipendente/prenotazioni-dipendente';
import {
  ImpostazioniDipendenteComponent
} from '@features/SezioneDipendente/Pagine/impostazioni-dipendente/impostazioni-dipendente';

export const DIPENDENTE_ROUTES: Routes = [
  {
    path: '',
    component: GeneralLayoutDipendenteSenzaAzienda,
    children: [
      { path: '', redirectTo: 'homepage', pathMatch: 'full' },
      { path: 'homepage', component: DashboardDipendente },
      {path:'prenotazioni',component:PrenotazioniDipendente},
      {path:'nuovaPrenotazione',component:CreaPrenotazione},
      {path:'impostazioni',component:ImpostazioniDipendenteComponent}
    ]
  }]
