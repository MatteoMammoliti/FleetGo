import {Component, OnInit} from '@angular/core';
import {DashboardService} from '@features/SezioneAdminAziendale/ServiceSezioneAdminAziendale/dashboard-service';
import {CaroselloOfferte} from '@features/SezioneAdminAziendale/Componenti/carosello-offerte/carosello-offerte';
import {OffertaDTO} from '@core/models/offertaDTO.models';
import {CaroselloRichiesteMiste} from '@features/SezioneAdminAziendale/Componenti/carosello-richieste-miste/carosello-richieste-miste';
import {CardStatisticheDashboard} from '@features/SezioneAdminAziendale/Componenti/card-statistiche-dashboard/card-statistiche-dashboard';
import {CurrencyPipe} from '@angular/common';
import {ModaleRichiestaAppuntamento} from '@features/SezioneAdminAziendale/Componenti/modale-richiesta-appuntamento/modale-richiesta-appuntamento';
import {of} from 'rxjs';

@Component({
  selector: 'app-dashboard-azienda',
  imports: [CaroselloOfferte, CaroselloRichiesteMiste, CurrencyPipe, CardStatisticheDashboard, ModaleRichiestaAppuntamento],
  templateUrl: './dashboard-azienda.html',
  styleUrl: './dashboard-azienda.css',
})

export class DashboardAzienda implements OnInit{

  constructor(private dashboardService:DashboardService) {}

  offerteAttive: OffertaDTO[] = [];
  contatoreRichiesteAffiliazione = 0;
  contatoreRichiesteNoleggio = 0;
  contatorePatentiDaAccettare = 0;

  statisticheGuadagno = 0;
  statisticheFlotta = 0;

  nomeAziendaGestita = "";
  nomeECognomeAdmin = "";

  offertaSelezionata: OffertaDTO = {} as OffertaDTO;
  modaleAppuntamentoVisibile = false;
  appuntamentoRichiesto = false;


  get sommaRichieste(): number {
    return this.contatoreRichiesteAffiliazione +
      this.contatoreRichiesteNoleggio +
      this.contatorePatentiDaAccettare;
  }

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

    this.dashboardService.getNumPatentiDaAccettare().subscribe({
      next: value => {
        console.log(value)
        if(value) this.contatorePatentiDaAccettare = value;
      }, error: err => { console.error(err); }
    })
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

  apriModaleRichiestaAppuntamento(offerta: OffertaDTO) {
    this.modaleAppuntamentoVisibile = true;
    this.offertaSelezionata = offerta;
  }

  chiudiModaleRichiestaAppuntamento() {
    this.modaleAppuntamentoVisibile = false;
    this.offertaSelezionata = {} as OffertaDTO;
  }

  richiediAppuntamento() {
    this.dashboardService.inoltraRichiestaDiAppuntamento().subscribe({
      next: value => {
        if(value) {
          this.appuntamentoRichiesto = true;
          setInterval( () => {
            this.chiudiModaleRichiestaAppuntamento();
          }, 3000)
        }
      }, error: err => { console.error(err); }
    })
  }
}
