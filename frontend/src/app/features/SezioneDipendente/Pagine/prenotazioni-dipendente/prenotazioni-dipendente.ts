import {Component, OnInit} from '@angular/core';
import {RichiestaNoleggioDTO} from '@core/models/richiestaNoleggioDTO.models';
import {PrenotazioneCard} from '@features/SezioneDipendente/componenti/prenotazione-card/prenotazione-card';
import {PrenotazioniService} from '@features/SezioneDipendente/ServiceSezioneDipendente/prenotazioni-service';
import {Router} from '@angular/router';
import { IntestazioneEBackground } from '@shared/Componenti/Ui/intestazione-ebackground/intestazione-ebackground';
import { MessaggioCardVuota } from '@shared/Componenti/Ui/messaggio-card-vuota/messaggio-card-vuota';
import { CommonModule } from '@angular/common';
import { BottonePillola } from '@shared/Componenti/Ui/bottone-pillola/bottone-pillola';

@Component({
  selector: 'app-prenotazioni-dipendente',
  imports: [
    PrenotazioneCard,
    IntestazioneEBackground,
    MessaggioCardVuota
    ,CommonModule,
    BottonePillola
  ],
  templateUrl: './prenotazioni-dipendente.html',
  styleUrl: './prenotazioni-dipendente.css',
})

export class PrenotazioniDipendente implements OnInit {
  constructor(private service:PrenotazioniService,
              private router:Router) {}

  prenotazioni:RichiestaNoleggioDTO[]=[];
  daVisualizzare:RichiestaNoleggioDTO[]=[]
  filtroAttivo:string="Tutte"

  ngOnInit(){
    this.getRichiesteDipendente();
  }

  getRichiesteDipendente(){
    this.service.richiediPrenotazioniDipendente().subscribe({
      next:(risposta:RichiestaNoleggioDTO[])=>{
        this.prenotazioni=risposta
        this.daVisualizzare=risposta
        this.impostaFiltro(this.filtroAttivo);
    },
      error:(err)=>console.log("Errore nel caricare i noleggi dei dipendente")
    });
  }

  eliminaPrenotazione(idPrenotazione:number){
    this.service.eliminaPrenotazione(idPrenotazione).subscribe({
      next:(risposta:any)=>{
        this.getRichiesteDipendente()
        console.log("Eliminata")
      },
      error:(err)=>console.log("Errore durante l'eliminazione")
    });
  }

  clickNuovaPrenotazione(){
    this.router.navigate(['/dashboardDipendente/nuovaPrenotazione'])
  }

  get prenotazioniFiltrate() {
    if(this.filtroAttivo === 'Tutte'){
      return this.prenotazioni;
    }

    return this.prenotazioni.filter(prenotazione => {

      let stato: boolean | undefined = false;

      if(this.filtroAttivo === 'Rifiutate'){
        stato = prenotazione.richiestaAnnullata;
      } else {
        stato = prenotazione.statoRichiesta == this.filtroAttivo && !prenotazione.richiestaAnnullata;
      }

      return stato;
    });
  }

  impostaFiltro(categoria: string) { this.filtroAttivo = categoria; }
}
