import {Component, OnInit} from '@angular/core';
import {CardStatisticheDashboardFleet} from '@shared/Componenti/Ui/card-statistiche-dashboard-fleet/card-statistiche-dashboard-fleet';
import {Router} from '@angular/router';
import {DashboardFleetGoService} from '@features/SezioneFleetGo/ServiceSezioneFleetGo/dashboardFleetGo-service';
import {ContenitoreStatisticheNumeriche} from '@core/models/ContenitoreStatisticheNumeriche';
import {FatturaDaGenerareDTO} from '@core/models/FatturaDaGenerareDTO';
import {FattureDaGenerare} from '@features/SezioneFleetGo/Componenti/fatture-da-generare/fatture-da-generare';
import {RichiesteManutenzioneDaGestire} from '@features/SezioneFleetGo/Componenti/richieste-manutenzione-da-gestire/richieste-manutenzione-da-gestire';
import {RichiestaManutenzioneDTO} from '@core/models/RichiestaManutenzioneDTO';
import {GestisciRichiestaManutenzione} from '@features/SezioneFleetGo/Componenti/gestisci-richiesta-manutenzione/gestisci-richiesta-manutenzione';
import {ModaleGenerazioneFattura} from '@features/SezioneFleetGo/Componenti/modale-generazione-fattura/modale-generazione-fattura';
import {OffertaDTO} from '@core/models/offertaDTO.models';
import {IntestazioneEBackground} from '@shared/Componenti/Ui/intestazione-ebackground/intestazione-ebackground';
import {BannerErrore} from "@shared/Componenti/Ui/banner-errore/banner-errore";

@Component({
  selector: 'app-dashboard-fleet-go',
    imports: [CardStatisticheDashboardFleet, FattureDaGenerare, RichiesteManutenzioneDaGestire, GestisciRichiestaManutenzione, ModaleGenerazioneFattura, IntestazioneEBackground, BannerErrore],
  templateUrl: './dashboard-fleet-go.html',
  styleUrl: './dashboard-fleet-go.css',
})
export class DashboardFleetGo implements OnInit{
  constructor(private dashboardService:DashboardFleetGoService,private router: Router) {}

  statistiche: ContenitoreStatisticheNumeriche = {
    totaleVeicoli: 0,
    veicoliAssegnati: 0,
    veicoliManutenzione: 0,
    totaleAziende: 0,
    veicoliDisponibili: 0,
    veicoliNoleggiati: 0,
    fattureDaGenerare:0,
    guadagnoMensile:0
  };

  erroreBanner='';

  percentualeNoleggiati=0;
  descrizionePercentuale:string=""

  richiesteManutenzione:RichiestaManutenzioneDTO[]=[];
  richiestaSelezionata:RichiestaManutenzioneDTO | null = null;

  apriPaginaGenerazioneOfferte = false;

  fatture:FatturaDaGenerareDTO[]=[];
  offerteAttive: OffertaDTO[] = [];
  fatturaDaGenerare: FatturaDaGenerareDTO = {} as FatturaDaGenerareDTO;
  successoBanner="";

  ngOnInit(): void {
    this.richiediStatistiche();
    this.richiediFattureDaGenerare();
    this.richiediManutenzioniDaGestire();
  }

  richiediStatistiche() {
    this.dashboardService.richiediStatistiche().subscribe({
      next: (contenitore) => {
        this.statistiche = contenitore;

        if(this.statistiche.veicoliAssegnati==0){
          this.descrizionePercentuale="Nessun Veicolo Noleggiato"
        }
        else {
          this.descrizionePercentuale="Noleggiati "+this.statistiche.veicoliAssegnati+ " veicoli su "+ this.statistiche.totaleVeicoli;
        }
        this.percentualeNoleggiati=Math.trunc((this.statistiche.veicoliAssegnati/this.statistiche.totaleVeicoli)*100);
      },
      error: (err) => {
        this.gestisciErrore(err.error);
      }
    });
  }

  richiediFattureDaGenerare(){
    this.dashboardService.richiediFattureDaGenerare().subscribe({
      next:(fattura)=>{
        this.fatture=fattura
      },
      error:(err:any)=>{
        this.gestisciErrore(err.error);
      }
    });
  }

  richiediManutenzioniDaGestire(){
    this.dashboardService.richiediManutenzioneDaGestire().subscribe({
      next:(richiesta:RichiestaManutenzioneDTO[])=>{
        this.richiesteManutenzione=richiesta
      },
      error:(err:any)=>{
        this.gestisciErrore(err.error);
      }
    });
  }


  caricaRichiestaManutenzione(idManutenzione:number){
    this.dashboardService.richiediInformazioniSuManutenzioneDaGestire(idManutenzione).subscribe({
      next:(risultato:RichiestaManutenzioneDTO)=>{
        this.richiestaSelezionata=risultato;
      },
      error:(err:any)=>{
        this.gestisciErrore(err.error);
      }
    });
  }

  accettaRichiestaManutenzione(idManutenzione:number){
    this.dashboardService.accettaRichiestaManutenzione(idManutenzione).subscribe({
      next:(risultato:string)=>{
        this.richiediManutenzioniDaGestire();
        this.richiestaSelezionata=null;
        this.gestisciSuccesso("Manutenzione accettata con successo");
      },
      error:(err:any)=>{
        this.gestisciErrore(err.error);
      }
    });
  }

  rifiutaRichiesta(idManutenzione:number){
    this.dashboardService.rifiutaRichiestaManutezione(idManutenzione).subscribe({
      next:(risultato:string)=>{
        this.richiediManutenzioniDaGestire();
        this.richiestaSelezionata=null;
        this.gestisciSuccesso("Manutenzione rifiutata con successo");
      },
      error:(err:any)=>{
        this.gestisciErrore(err.error);
      }
    });
  }

  chiudiFinestraModale(){
    this.richiestaSelezionata=null;
    this.richiediManutenzioniDaGestire();
  }

  riceviFatturaDaGenerare(fattura: FatturaDaGenerareDTO) {
    this.fatturaDaGenerare = fattura;
    this.onClickGeneraFattura();
  }

  generaFattura(fattura:FatturaDaGenerareDTO){
    this.dashboardService.generaFattura(fattura).subscribe({
      next:()=>{
        this.richiediFattureDaGenerare();
        this.chiudiFinestraModaleGenerazioneFattura();
        this.gestisciSuccesso("Fattura emessa con successo");
      },
      error:(err:any)=>{
        this.gestisciErrore(err.error);
      }
    })
  }

  caricaOfferte() {
    this.dashboardService.getOfferteAttive().subscribe({
      next: value => {this.offerteAttive = value;},
      error: err => {
        this.gestisciErrore(err.error);
      }
    })
  }

  onClickGeneraFattura() {
    this.apriPaginaGenerazioneOfferte = true;
    this.caricaOfferte();
  }

  chiudiFinestraModaleGenerazioneFattura() { this.apriPaginaGenerazioneOfferte = false; }

  sezioneManutenzione(){
    const tabellaManutenzione = document.getElementById('sezioneManutezione');
    if (tabellaManutenzione) {
      tabellaManutenzione.scrollIntoView({ behavior: 'smooth', block: 'center' });
    }
  }

  sezioneFatturazione() {
    const tabellaFatturazione = document.getElementById('sezioneFatture');
    if (tabellaFatturazione) {
      tabellaFatturazione.scrollIntoView({behavior: 'smooth', block: 'center'});
    }
  }

  sezioneFlotta(){
    this.router.navigate(['/dashboardFleetGo/flotta-globale']);
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
