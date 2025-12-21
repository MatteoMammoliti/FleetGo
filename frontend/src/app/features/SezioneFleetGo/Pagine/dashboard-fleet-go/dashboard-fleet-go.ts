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

@Component({
  selector: 'app-dashboard-fleet-go',
  imports: [CardStatisticheDashboardFleet, FattureDaGenerare, RichiesteManutenzioneDaGestire, GestisciRichiestaManutenzione, ModaleGenerazioneFattura, IntestazioneEBackground],
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

  percentualeNoleggiati=0;
  descrizionePercentuale:string=""

  richiesteManutenzione:RichiestaManutenzioneDTO[]=[];
  richiestaSelezionata:RichiestaManutenzioneDTO | null = null;

  apriPaginaGenerazioneOfferte = false;

  fatture:FatturaDaGenerareDTO[]=[];
  offerteAttive: OffertaDTO[] = [];
  fatturaDaGenerare: FatturaDaGenerareDTO = {} as FatturaDaGenerareDTO;

  ngOnInit(): void {
    this.richiediStatistiche();
    this.richiediFattureDaGenerare();
    this.richiediManutenzioniDaGestire();
  }

  richiediStatistiche() {
    this.dashboardService.richiediStatistiche().subscribe({
      next: (contenitore) => {
        console.log(contenitore);
        this.statistiche = contenitore;

        if(this.statistiche.veicoliAssegnati==0){
          this.descrizionePercentuale="Nessun Veicolo Noleggiato"
        }
        else {
          this.descrizionePercentuale="Noleggiati "+this.statistiche.veicoliAssegnati+ " veicoli su "+ this.statistiche.totaleVeicoli;
        }
        this.percentualeNoleggiati=Math.trunc((this.statistiche.veicoliAssegnati/this.statistiche.totaleVeicoli)*100);
      },
      error: (err) => console.error("Errore richiesta statistiche:", err)
    });
  }

  richiediFattureDaGenerare(){
    this.dashboardService.richiediFattureDaGenerare().subscribe({
      next:(fattura)=>{
        this.fatture=fattura
      },
      error:(err:any)=>(console.error("Errore richiesta fatture da generare",err))
    });
  }

  richiediManutenzioniDaGestire(){
    this.dashboardService.richiediManutenzioneDaGestire().subscribe({
      next:(richiesta:RichiestaManutenzioneDTO[])=>{
        console.log(richiesta)
        this.richiesteManutenzione=richiesta
      },
      error:(err:any)=>(console.error("Errore richieste manutenzioni da gestire",err))
    });
  }


  caricaRichiestaManutenzione(idManutenzione:number){
    this.dashboardService.richiediInformazioniSuManutenzioneDaGestire(idManutenzione).subscribe({
      next:(risultato:RichiestaManutenzioneDTO)=>{
        this.richiestaSelezionata=risultato
      },
      error:(err:any)=>(console.error("errore nella visualizzazione delle informazioni della richiesta",err))
    });
  }

  accettaRichiestaManutenzione(idManutenzione:number){
    this.dashboardService.accettaRichiestaManutenzione(idManutenzione).subscribe({
      next:(risultato:string)=>{
        console.log(risultato)
        this.richiediManutenzioniDaGestire();
        this.richiestaSelezionata=null;
      },
      error:(err:any)=>{console.error("Errore nell'accettare la richiesta di manutenzione",err)}
    });
  }

  rifiutaRichiesta(idManutenzione:number){
    this.dashboardService.rifiutaRichiestaManutezione(idManutenzione).subscribe({
      next:(risultato:string)=>{
        this.richiediManutenzioniDaGestire();
        this.richiestaSelezionata=null;
      },
      error:(err:any)=>{console.error("Errore nel rifiutare la richiesta di mantezione",err)}
    });
  }

  chiudiFinestraModale(){
    this.richiestaSelezionata=null;
    this.richiediManutenzioniDaGestire();
  }

  riceviFatturaDaGenerare(fattura: FatturaDaGenerareDTO) {
    this.fatturaDaGenerare = fattura;
    console.log("ho ricevuto", this.fatturaDaGenerare)
    this.onClickGeneraFattura();
  }

  generaFattura(fattura:FatturaDaGenerareDTO){
    console.log("sto inviando al back", fattura)
    this.dashboardService.generaFattura(fattura).subscribe({
      next:()=>{
        this.richiediFattureDaGenerare();
        this.chiudiFinestraModaleGenerazioneFattura();
      },
      error:(err:any)=>{console.error("Errore nella generazione della fattura",err)}
    })
  }

  caricaOfferte() {
    this.dashboardService.getOfferteAttive().subscribe({
      next: value => {this.offerteAttive = value;},
      error: err => { console.error(err); }
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
}
