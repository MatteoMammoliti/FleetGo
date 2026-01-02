import {Component, OnInit} from '@angular/core';
import {DashboardService} from '@features/SezioneAdminAziendale/ServiceSezioneAdminAziendale/dashboard-service';
import {CaroselloOfferte} from '@features/SezioneAdminAziendale/Componenti/carosello-offerte/carosello-offerte';
import {OffertaDTO} from '@core/models/offertaDTO.models';
import {CaroselloRichiesteMiste} from '@features/SezioneAdminAziendale/Componenti/carosello-richieste-miste/carosello-richieste-miste';
import {CardStatisticheDashboard} from '@features/SezioneAdminAziendale/Componenti/card/card-statistiche-dashboard/card-statistiche-dashboard';
import {CurrencyPipe} from '@angular/common';
import {ModaleRichiestaAppuntamento} from '@features/SezioneAdminAziendale/Componenti/modali/modale-richiesta-appuntamento/modale-richiesta-appuntamento';
import {ModaleObbligoImpostazioneSede} from '@features/SezioneAdminAziendale/Componenti/modali/modale-obbligo-impostazione-sede/modale-obbligo-impostazione-sede';
import {LuogoDTO} from '@core/models/luogoDTO.models';
import {ModificaDatiService} from '@features/SezioneAdminAziendale/ServiceSezioneAdminAziendale/modifica-dati-service';
import {
  GraficoTortaFlotta
} from '@features/SezioneAdminAziendale/Componenti/grafici/grafico-torta-flotta/grafico-torta-flotta';
import {
  CardStatisticheDashboardFleet
} from '@shared/Componenti/Ui/card-statistiche-dashboard-fleet/card-statistiche-dashboard-fleet';
import {TemplateTitoloSottotitolo} from '@shared/Componenti/Ui/template-titolo-sottotitolo/template-titolo-sottotitolo';

@Component({
  selector: 'app-dashboard-azienda',
  imports: [CaroselloOfferte,
    CaroselloRichiesteMiste,
    CurrencyPipe,
    GraficoTortaFlotta, CardStatisticheDashboardFleet, TemplateTitoloSottotitolo],
  templateUrl: './dashboard-azienda.html',
  styleUrl: './dashboard-azienda.css',
})

export class DashboardAzienda implements OnInit{

  constructor(private dashboardService:DashboardService,
              private modificaDatiService: ModificaDatiService) {}

  veicoliInUso = 6;
  veicoliDisponibili = 3;
  veicoliInManutenzione = 1;

  offerteAttive: OffertaDTO[] = [];
  contatoreRichiesteAffiliazione = 0;
  contatoreRichiesteNoleggio = 0;
  contatoreFattureDaPagare = 0;

  statisticheGuadagno = 0;
  statisticheFlotta = 0;

  nomeAziendaGestita = "";
  nomeECognomeAdmin = "";

  offertaSelezionata: OffertaDTO = {} as OffertaDTO;
  modaleAppuntamentoVisibile = false;
  appuntamentoRichiesto = false;

  nessunaSedeImpostata = false;

  get sommaRichieste(): number {
    return this.contatoreRichiesteAffiliazione +
      this.contatoreRichiesteNoleggio +
      this.contatoreFattureDaPagare;
  }

  ngOnInit(){
    this.caricaOfferteAttive();
    this.caricaContatori();
    this.inizializzaStatistiche();

    this.dashboardService.getNomeAziendaGestita().subscribe({
      next: value => {
        if(value) {
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
    })

    this.dashboardService.isSedeImpostata().subscribe({
      next: value => { this.nessunaSedeImpostata = !value;
      }, error: err => { console.error(err); }
    })
  }


  caricaOfferteAttive() {
    this.dashboardService.getOfferteAttive().subscribe({
      next: value => {
        if(value !== null && value !== undefined) this.offerteAttive = value;
      }, error: err => { console.error(err); }
    })
  }

  caricaContatori() {
    this.dashboardService.getContatoreRichiesteAffiliazione().subscribe({
      next: value => {
        if(value !== null && value !== undefined) this.contatoreRichiesteAffiliazione = value;
      }, error: error => { console.error(error); }
    })

    this.dashboardService.getContatoreRichiesteNoleggio().subscribe({
      next: value => {
        if(value !== null && value !== undefined) this.contatoreRichiesteNoleggio = value;
      }, error: error => { console.error(error); }
    })

    this.dashboardService.getNumFattureDaPagare().subscribe({
      next: value => {
        if(value !== null && value !== undefined) this.contatoreFattureDaPagare = value;
      }, error: error => { console.error(error); }
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

  impostaSede(luogo: LuogoDTO) {
    this.modificaDatiService.aggiungiLuogo(luogo).subscribe({
      next: value => {
        if(value) {
          this.nessunaSedeImpostata = false;
        }
      }, error: err => { console.error(err); }
    })
  }
}
