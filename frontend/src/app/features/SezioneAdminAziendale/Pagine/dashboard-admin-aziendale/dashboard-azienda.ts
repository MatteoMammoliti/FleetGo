import {Component, OnInit} from '@angular/core';
import {DashboardService} from '@features/SezioneAdminAziendale/ServiceSezioneAdminAziendale/dashboard-service';
import {CaroselloOfferte} from '@features/SezioneAdminAziendale/Componenti/carosello-offerte/carosello-offerte';
import {OffertaDTO} from '@core/models/offertaDTO.models';
import {CaroselloRichiesteMiste} from '@features/SezioneAdminAziendale/Componenti/carosello-richieste-miste/carosello-richieste-miste';
import {CardStasticheDashboard} from '@features/SezioneAdminAziendale/Componenti/card-stastiche-dashboard/card-stastiche-dashboard';

interface Statistica {
  valore: any,
}

@Component({
  selector: 'app-dashboard-azienda',
  imports: [CaroselloOfferte, CaroselloRichiesteMiste, CardStasticheDashboard],
  templateUrl: './dashboard-azienda.html',
  styleUrl: './dashboard-azienda.css',
})

export class DashboardAzienda implements OnInit{

  constructor(private dashboardService:DashboardService) {}

  offerteAttive: OffertaDTO[] = [];
  contatoreRichiesteAffiliazione = 0;
  contatoreRichiesteNoleggio = 0;

  statisticheGuadagno = 0;
  statisticheFlotta = 0;

  ngOnInit(){
    this.caricaOfferteAttive();
    this.caricaContatori();
    this.inizializzaStatistiche();
  }

  caricaOfferteAttive() {
    this.dashboardService.getOfferteAttive().subscribe({
      next: value => {
        if(value) this.offerteAttive = value;
      }, error: err => { console.error(err); }
    })
  }

  caricaContatori() {
    this.dashboardService.getContatoreRichiesteAffiliazione().subscribe({
      next: value => {
        console.log("affiliazione", value)
        if(value) this.contatoreRichiesteAffiliazione = value;
      }, error: error => { console.error(error); }
    })

    this.dashboardService.getContatoreRichiesteNoleggio().subscribe({
      next: value => {
        console.log("noleggio", value)
        if(value) this.contatoreRichiesteNoleggio = value;
      }, error: error => { console.error(error); }
    })
  }

  inizializzaStatistiche() {

    this.dashboardService.getSpesaMensile().subscribe({
      next: value => {
        console.log(value)
        if(value) this.statisticheGuadagno = value;
      }, error: err => { console.error(err); }
    })

    this.dashboardService.getNumeroNoleggi().subscribe({
      next: value => {
        if(value) this.statisticheFlotta = value;
      }, error: err => { console.error(err); }
    })
  }
}
