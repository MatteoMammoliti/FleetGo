import {Component, OnInit} from '@angular/core';
import {DashboardService} from '@features/SezioneAdminAziendale/ServiceSezioneAdminAziendale/dashboard-service';
import {CaroselloOfferte} from '@features/SezioneAdminAziendale/Componenti/carosello-offerte/carosello-offerte';
import {OffertaDTO} from '@core/models/offertaDTO.models';
import {CaroselloRichiesteMiste} from '@features/SezioneAdminAziendale/Componenti/carosello-richieste-miste/carosello-richieste-miste';

@Component({
  selector: 'app-dashboard-azienda',
  imports: [CaroselloOfferte, CaroselloRichiesteMiste],
  templateUrl: './dashboard-azienda.html',
  styleUrl: './dashboard-azienda.css',
})

export class DashboardAzienda implements OnInit{

  constructor(private dashboardService:DashboardService) {}

  offerteAttive: OffertaDTO[] = [];
  contatoreRichiesteAffiliazione = 0;
  contatoreRichiesteNoleggio = 0;

  ngOnInit(){
    this.caricaOfferteAttive();
    this.caricaContatori();
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
}
