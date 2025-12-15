import {Component, OnInit} from '@angular/core';
import {DashboardService} from '@features/SezioneAdminAziendale/ServiceSezioneAdminAziendale/dashboard-service';
import {CaroselloOfferte} from '@features/SezioneAdminAziendale/Componenti/carosello-offerte/carosello-offerte';
import {OffertaDTO} from '@core/models/offertaDTO.models';
import {CaroselloRichiesteMiste} from '@features/SezioneAdminAziendale/Componenti/carosello-richieste-miste/carosello-richieste-miste';
import {CardStatisticheDashboard} from '@features/SezioneAdminAziendale/Componenti/card-statistiche-dashboard/card-statistiche-dashboard';
import {CurrencyPipe} from '@angular/common';

@Component({
  selector: 'app-dashboard-azienda',
  imports: [CaroselloOfferte, CaroselloRichiesteMiste, CurrencyPipe, CardStatisticheDashboard],
  templateUrl: './dashboard-azienda.html',
  styleUrl: './dashboard-azienda.css',
})

export class DashboardAzienda implements OnInit{

  constructor(private dashboardService:DashboardService) {}

  offerteAttive: OffertaDTO[] = [];
  contatoreRichiesteAffiliazione = 0;
  contatoreRichiesteNoleggio = 0;

  richiesteDipendentiSimulate = 2;
  sommaRichieste = 0;

  statisticheGuadagno = 0;
  statisticheFlotta = 0;

  nomeAziendaGestita = "";
  nomeECognomeAdmin = "";

  ngOnInit(){
    this.caricaOfferteAttive();
    this.caricaContatori();
    this.inizializzaStatistiche();

    this.dashboardService.getNomeAziendaGestita().subscribe({
      next: value => {
        if(value) {
          console.log(value)
          this.nomeAziendaGestita = value;
        }
      }, error: err => { console.error(err); }
    })

    this.dashboardService.getNomeCognomeAdmin().subscribe({
      next: value => {
        if(value) {
          this.nomeECognomeAdmin = value;
        }
      }, error: err => { console.error(err); }
    })}

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
        if(value) this.contatoreRichiesteAffiliazione = value;
      }, error: error => { console.error(error); }
    })

    this.dashboardService.getContatoreRichiesteNoleggio().subscribe({
      next: value => {
        if(value) this.contatoreRichiesteNoleggio = value;
      }, error: error => { console.error(error); }
    })

    this.sommaRichieste = this.contatoreRichiesteAffiliazione + this.contatoreRichiesteNoleggio + this.richiesteDipendentiSimulate;
  }

  inizializzaStatistiche() {

    this.dashboardService.getSpesaMensile().subscribe({
      next: value => {
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
