import { Component } from '@angular/core';
import {CardStatisticheDashboardFleet} from '@shared/Componenti/Ui/card-statistiche-dashboard-fleet/card-statistiche-dashboard-fleet';
import {RouterLink} from '@angular/router';
import {DashboardFleetGoService} from '@features/SezioneFleetGo/ServiceSezioneFleetGo/dashboardFleetGo-service';
import {ContenitoreStatisticheNumeriche} from '@core/models/ContenitoreStatisticheNumeriche';
import {FatturaDaGenerareDTO} from '@core/models/FatturaDaGenerareDTO';
import {FattureDaGenerare} from '@features/SezioneFleetGo/Componenti/fatture-da-generare/fatture-da-generare';
import {
  RichiesteManutenzioneDaGestire
} from '@features/SezioneFleetGo/Componenti/richieste-manutenzione-da-gestire/richieste-manutenzione-da-gestire';
import {RichiestaManutenzioneDTO} from '@core/models/RichiestaManutenzioneDTO';
import {
  GestisciRichiestaManutenzione
} from '@features/SezioneFleetGo/Componenti/gestisci-richiesta-manutenzione/gestisci-richiesta-manutenzione';

@Component({
  selector: 'app-dashboard-fleet-go',
  imports: [CardStatisticheDashboardFleet, FattureDaGenerare, RichiesteManutenzioneDaGestire, GestisciRichiestaManutenzione],
  templateUrl: './dashboard-fleet-go.html',
  styleUrl: './dashboard-fleet-go.css',
})
export class DashboardFleetGo {
  constructor(private dashboardService:DashboardFleetGoService) {}

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

  richiesteManutenzione:RichiestaManutenzioneDTO[]=[];
  richiestaSelezionata:RichiestaManutenzioneDTO | null = null;

  fatture:FatturaDaGenerareDTO[]=[];

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

  protected readonly Math = Math;
}
