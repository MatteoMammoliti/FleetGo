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


  erroreBanner='';
  successoBanner='';

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
        this.gestisciErrore(err.error);
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
        this.gestisciErrore(err.error);
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
      this.gestisciErrore(err.error);
    }
  });
  }
  concludiRichiestaManutenzione(idRichiesta:number){
    this.service.chiudiRichiestaManutenzione(idRichiesta).subscribe({
      next:(risposta:string)=>{
        this.prelevaManutenzioniStorico()
        this.prelevaManutenzioniInCorso()

        this.gestisciSuccesso('Manutenzione conclusa con successo');

      },
      error:(err)=>{
        this.gestisciErrore(err.error);
      }
    })

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
