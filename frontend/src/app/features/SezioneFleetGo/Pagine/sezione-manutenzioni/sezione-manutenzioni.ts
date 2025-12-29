import { Component } from '@angular/core';
import {ContenitoreStatisticheNumericheManutezioni} from '@core/models/ContenitoreStatisticheNumericheManutezioni';
import {SezioneManutenzioneService} from '@features/SezioneFleetGo/ServiceSezioneFleetGo/sezione-manutenzione-service';
import {
  TabellaManutenzioniInCorso
} from '@features/SezioneFleetGo/Componenti/tabella-manutenzioni-in-corso/tabella-manutenzioni-in-corso';
import {RichiestaManutenzioneDTO} from '@core/models/RichiestaManutenzioneDTO';
import {
  TabellaStoricoManutenzioni
} from '@features/SezioneFleetGo/Componenti/tabella-storico-manutenzioni/tabella-storico-manutenzioni';
import {IntestazioneEBackground} from '@shared/Componenti/Ui/intestazione-ebackground/intestazione-ebackground';
import {
  CardStatisticheDashboardFleet
} from '@shared/Componenti/Ui/card-statistiche-dashboard-fleet/card-statistiche-dashboard-fleet';
import {BannerErrore} from '@shared/Componenti/Ui/banner-errore/banner-errore';

@Component({
  selector: '',
  imports: [

    TabellaManutenzioniInCorso,
    TabellaStoricoManutenzioni,
    IntestazioneEBackground,
    CardStatisticheDashboardFleet,
    BannerErrore,
  ],
  templateUrl: './sezione-manutenzioni.html',
  styleUrl: './sezione-manutenzioni.css',
})
export class SezioneManutenzioni {
constructor(private service:SezioneManutenzioneService) {}
  datiManutenzione:ContenitoreStatisticheNumericheManutezioni={
    interventiConclusi:0,
    attualmenteInOfficina:0
  }
  listeManutezioniInCorso:RichiestaManutenzioneDTO[]=[]
  listaManutenzioniStorico:RichiestaManutenzioneDTO[]=[]


  erroreBanner="";

  ngOnInit(){
  this.prelevaDatiManutenzioni();
  this.prelevaManutenzioniInCorso();
  this.prelevaManutenzioniStorico();
  }

  prelevaDatiManutenzioni(){
    this.service.prelevaDatiManutenzione().subscribe({
      next:(response:ContenitoreStatisticheNumericheManutezioni)=>{
        this.datiManutenzione=response
    },
      error:(err) => {
        console.error("Errore nel prelevare le statistiche delle manutenzioni");
        this.erroreBanner="Errore nel prelevare le statistiche delle manutenzioni";
      }
    });
  }

  prelevaManutenzioniInCorso(){
    this.service.prelevaManutezioniInCorso().subscribe({
      next:(response:RichiestaManutenzioneDTO[])=>{
        console.log(response);
        this.listeManutezioniInCorso=response;
      },
      error:(err)=>{
        console.error("Errore nel caricamento delle manutezioni in corso")
        this.erroreBanner="Errore nel caricamento delle manutezioni in corso";
      }
    });
  }
  prelevaManutenzioniStorico(){
  this.service.prelevaManutenzioniStorico().subscribe({
    next:(reponse:RichiestaManutenzioneDTO[])=>{
      this.listaManutenzioniStorico=reponse
      console.log(reponse)
    },
    error:(err)=>{
      console.error("Errore nel caricamento dello storico delle manutenzioni");
      this.erroreBanner="Errore nel caricamento dello storico delle manutenzioni";
      }
  });
  }
  concludiRichiestaManutenzione(idRichiesta:number){
    this.service.chiudiRichiestaManutenzione(idRichiesta).subscribe({
      next:(risposta:string)=>{
        this.prelevaManutenzioniStorico()
        this.prelevaManutenzioniInCorso()

      },
      error:(err)=>{
        console.error("Errore nel chiudere la manutenzione")
        this.erroreBanner="Errore nel chiudere la richiesta di manutenzione";
    }
    })

  }
}
