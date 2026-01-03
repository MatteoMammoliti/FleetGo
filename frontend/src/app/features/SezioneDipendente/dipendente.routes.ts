import {Routes} from '@angular/router';
import {DashboardDipendente} from '@features/SezioneDipendente/Pagine/dashboard-dipendente/dashboard-dipendente';
import {CreaPrenotazione} from '@features/SezioneDipendente/Pagine/crea-prenotazione/crea-prenotazione';
import {PrenotazioniDipendente} from '@features/SezioneDipendente/Pagine/prenotazioni-dipendente/prenotazioni-dipendente';
import {ImpostazioniDipendenteComponent} from '@features/SezioneDipendente/Pagine/impostazioni-dipendente/impostazioni-dipendente';
import {GeneralLayoutDipendente} from '../../layouts/general-layout-dipendente/general-layout-dipendente';

export const DIPENDENTE_ROUTES: Routes = [
  {
    path: '',
    component: GeneralLayoutDipendente,
    children: [
      { path: '', redirectTo: 'homepage', pathMatch: 'full' },
      { path: 'homepage', component: DashboardDipendente },
      {path:'prenotazioni',component:PrenotazioniDipendente},
      {path:'nuovaPrenotazione',component:CreaPrenotazione},
      {path:'impostazioni',component:ImpostazioniDipendenteComponent}
    ]
  }]
