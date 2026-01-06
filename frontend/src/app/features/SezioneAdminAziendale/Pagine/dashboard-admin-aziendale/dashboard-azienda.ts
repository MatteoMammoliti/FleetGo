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
import {Router} from '@angular/router';
import {BannerErrore} from '@shared/Componenti/Ui/banner-errore/banner-errore';

@Component({
  selector: 'app-dashboard-azienda',
  imports: [CaroselloOfferte,
    CaroselloRichiesteMiste,
    CurrencyPipe,
    GraficoTortaFlotta, CardStatisticheDashboardFleet, TemplateTitoloSottotitolo, ModaleRichiestaAppuntamento, ModaleObbligoImpostazioneSede, BannerErrore],
  templateUrl: './dashboard-azienda.html',
  styleUrl: './dashboard-azienda.css',
})

export class DashboardAzienda implements OnInit{

  constructor(private dashboardService:DashboardService,
              private modificaDatiService: ModificaDatiService, private router:Router) {}

  veicoliInUso = 0;
  veicoliDisponibili = 0;
  veicoliInManutenzione = 0;

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

  nessunaSedeImpostata = false;

  richiesteContattoInCorso = new Set<any>();

  erroreBanner="";
  successoBanner="";

  get sommaRichieste(): number {
    return this.contatoreRichiesteAffiliazione +
      this.contatoreRichiesteNoleggio +
      this.contatoreFattureDaPagare;
  }

  ngOnInit(){
    this.caricaOfferteAttive();
    this.caricaContatori();
    this.inizializzaStatistiche();
    this.caricaDatiGrafico();

    this.dashboardService.getNomeAziendaGestita().subscribe({
      next: value => {
        if(value) {
          this.nomeAziendaGestita = value;
        }
      }, error: err => { this.gestisciErrore(err.error())  }
    })

    this.dashboardService.getNomeCognomeAdmin().subscribe({
      next: value => {
        if(value) {
          this.nomeECognomeAdmin = value;
        }
      }, error: err => { this.gestisciErrore(err.error()) }
    })

    this.dashboardService.isSedeImpostata().subscribe({
      next: value => { this.nessunaSedeImpostata = !value;
      }, error: err => { this.gestisciErrore(err.error())  }
    })

    const salvate = localStorage.getItem('richieste_effettuate');
    if (salvate) {
      this.richiesteContattoInCorso = new Set(JSON.parse(salvate));
    }
  }


  caricaDatiGrafico(){
    this.dashboardService.getDatiGraficoTorta().subscribe({
      next: value => {
        if(value !== null && value !== undefined) {
          this.veicoliInUso=value.veicoliNoleggiati;
          this.veicoliDisponibili = value.veicoliDisponibili;
          this.veicoliInManutenzione = value.veicoliManutenzione;
        }
        },
        error: err => { this.gestisciErrore(err.error())  }
    })
  }


  caricaOfferteAttive() {
    this.dashboardService.getOfferteAttive().subscribe({
      next: value => {
        if(value !== null && value !== undefined) this.offerteAttive = value;
      }, error: err => { this.gestisciErrore(err.error())  }
    })
  }

  caricaContatori() {
    this.dashboardService.getContatoreRichiesteAffiliazione().subscribe({
      next: value => {
        if(value !== null && value !== undefined) this.contatoreRichiesteAffiliazione = value;
      }, error: err => { this.gestisciErrore(err.error())  }
    })

    this.dashboardService.getContatoreRichiesteNoleggio().subscribe({
      next: value => {
        if(value !== null && value !== undefined) this.contatoreRichiesteNoleggio = value;
      }, error: err => { this.gestisciErrore(err.error())  }
    })

    this.dashboardService.getNumFattureDaPagare().subscribe({
      next: value => {
        if(value !== null && value !== undefined) this.contatoreFattureDaPagare = value;
      }, error: err => { this.gestisciErrore(err.error())  }
    })
  }

  inizializzaStatistiche() {

    this.dashboardService.getSpesaMensile().subscribe({
      next: value => {
        if(value) this.statisticheGuadagno = value;
      }, error: err => { this.gestisciErrore(err.error())  }
    })

    this.dashboardService.getNumeroAutoSenzaLuogo().subscribe({
      next: value => {
        if(value) this.statisticheFlotta = value;
      }, error: err => { this.gestisciErrore(err.error())  }
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
    const idOfferta = this.offertaSelezionata.idOfferta;
    if (!idOfferta || this.richiesteContattoInCorso.has(idOfferta)) return;

    this.dashboardService.inoltraRichiestaDiAppuntamento().subscribe({
      next: value => {
        if(value) {
          this.richiesteContattoInCorso.add(this.offertaSelezionata.idOfferta);
          localStorage.setItem('richieste_effettuate', JSON.stringify(Array.from(this.richiesteContattoInCorso)));
          this.gestisciSuccesso("Appuntamento richiesto con successo!");
          this.chiudiModaleRichiestaAppuntamento();

        }

      }, error: err => { this.gestisciErrore(err.error())  }
    })
  }

  impostaSede(luogo: LuogoDTO) {
    this.modificaDatiService.aggiungiLuogo(luogo).subscribe({
      next: value => {
        if(value) {
          this.nessunaSedeImpostata = false;
          this.gestisciSuccesso("Sede impostata con successo!");

        }
      }, error: err => { this.gestisciErrore(err.error()) }
    })
  }

  navigaFlottaSenzaLuogo() {
    this.router.navigate(['/dashboardAzienda/flotta'], {
      queryParams: { stato: 'SENZALUOGO' }
    });
  }


  gestisciErrore(messaggio: string) {
    this.successoBanner = '';
    this.erroreBanner = messaggio;
    setTimeout(() => this.erroreBanner = '', 5000);
  }

  gestisciSuccesso(messaggio: string) {
    this.erroreBanner = '';
    this.successoBanner = messaggio;
    setTimeout(() => this.successoBanner = '', 3000);
  }
}
