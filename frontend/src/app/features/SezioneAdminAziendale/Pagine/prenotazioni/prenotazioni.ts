import {Component, OnInit} from '@angular/core';
import {ModaleDettagliPrenotazione} from '@features/SezioneAdminAziendale/Componenti/modale-dettagli-prenotazione/modale-dettagli-prenotazione';
import {TabellaPrenotazioni} from '@features/SezioneAdminAziendale/Componenti/tabella-prenotazioni/tabella-prenotazioni';
import {RichiestaNoleggioDTO} from '@core/models/richiestaNoleggioDTO.models';
import {PrenotazioniService} from '@features/SezioneAdminAziendale/ServiceSezioneAdminAziendale/prenotazioni-service';
import {
  BannerNoleggiDaApprovare
} from '@features/SezioneAdminAziendale/Componenti/banner-noleggi-da-approvare/banner-noleggi-da-approvare';
import {FormsModule} from '@angular/forms';
import {
  ModaleApprovazioneNoleggi
} from '@features/SezioneAdminAziendale/Componenti/modale-approvazione-noleggi/modale-approvazione-noleggi';
import {RisoluzioneConfilittiNoleggio} from '@core/models/RisoluzioneConfilittiNoleggio';

@Component({
  selector: 'app-prenotazioni',
  imports: [
    ModaleDettagliPrenotazione,
    TabellaPrenotazioni,
    BannerNoleggiDaApprovare,
    FormsModule,
    ModaleApprovazioneNoleggi
  ],
  templateUrl: './prenotazioni.html',
  styleUrl: './prenotazioni.css',
})
export class Prenotazioni implements OnInit{

  constructor(private prenotazioniService:PrenotazioniService) {}

  richiesteNoleggio: RichiestaNoleggioDTO[] = [];
  modaleDettaglioAperto = false;
  dettaglioDellaPrenotazione = {} as RichiestaNoleggioDTO;

  filtroStato = "";
  filtroDataInizio = "";
  filtroDataFine = "";

  numeroNoleggiDaApprovare = 0;
  richiesteDaApprovare: RichiestaNoleggioDTO[] = [];
  modaleAccettazioneAperto = false;

  ngOnInit() {
    this.getPrenotazioni()
    this.getNumeroNoleggiDaApprovare();
  }

  getPrenotazioni() {
    this.prenotazioniService.getPrenotazioni().subscribe({
      next: value => {
        if(value) this.richiesteNoleggio = value;
      }, error: err => {
        console.error(err);
      }
    })
  }

  getNumeroNoleggiDaApprovare() {
    this.prenotazioniService.getNumeroNoleggiDaApprovare().subscribe({
      next: value => {
        if(value !== undefined && value !== null) this.numeroNoleggiDaApprovare = value;
      }, error: err => { console.error(err); }
    })
  }

  get prenotazioniFiltrate() {
    return this.richiesteNoleggio.filter(richiestaNoleggio => {

      const stato = richiestaNoleggio.statoRichiesta ? richiestaNoleggio.statoRichiesta?.toLowerCase() : "";
      const filtro = this.filtroStato ? this.filtroStato.toLowerCase() : "";

      let matchStato = true;

      if(filtro && filtro != "tutti") {
        matchStato = false;

        if(filtro === "in corso") {
          matchStato = stato === "in corso";
        }
        else if (filtro === "da ritirare") {
          matchStato = stato === "da ritirare";
        }
        else if (filtro === "terminate") {
          matchStato = stato === "terminata";
        }
      }

      const dataNoleggio = richiestaNoleggio.dataRitiro ? richiestaNoleggio.dataRitiro.toString().substring(0, 7) : '';

      let matchData = true;
      if (this.filtroDataInizio) matchData = matchData && dataNoleggio >= this.filtroDataInizio;
      if (this.filtroDataFine) matchData = matchData && dataNoleggio <= this.filtroDataFine;

      return matchStato && matchData;
    })
  }

  apriModale(idRichiesta: number) {
    this.prenotazioniService.getPrenotazioneDettagliata(idRichiesta).subscribe({
      next: value => {
        if(value) this.dettaglioDellaPrenotazione = value;
      }, error: err => {
        console.error(err);
      }
    })
    this.modaleDettaglioAperto = true;
  }

  chiudiModale() {
    this.dettaglioDellaPrenotazione = {} as RichiestaNoleggioDTO;
    this.modaleDettaglioAperto = false;
  }

  resettaFiltri() {
    this.filtroStato = "";
    this.filtroDataInizio = "";
    this.filtroDataFine = "";
  }

  gestisciVisibilitaModaleAccettazioneNoleggi() {
    this.modaleAccettazioneAperto = !this.modaleAccettazioneAperto;

    if(this.modaleAccettazioneAperto) {
      this.prenotazioniService.getPrenotazioniDaAccettare().subscribe({
        next: value => {
          if(value) this.richiesteDaApprovare = value;
        }, error: err => { console.error(err); }
      })
    } else {
      this.richiesteDaApprovare = [];
    }
  }

  approvaRichiesta(idRichiesta: number) {
    this.prenotazioniService.approvaRichiesta(idRichiesta).subscribe({
      next: value => {
        if(value) {
          this.getPrenotazioni();
          this.getNumeroNoleggiDaApprovare();
          this.resettaFiltri();
          this.gestisciVisibilitaModaleAccettazioneNoleggi();
        }
      }, error: err => { console.error(err); }
    })
  }

  rifiutaRichiesta(idRichiesta: number) {
    this.prenotazioniService.rifiutaRichiesta(idRichiesta).subscribe({
      next: value => {
        if(value) {
          this.getPrenotazioni();
          this.getNumeroNoleggiDaApprovare();
          this.resettaFiltri();
          this.gestisciVisibilitaModaleAccettazioneNoleggi();
        }
      }, error: err => { console.error(err); }
    })
  }

  accettazioneConRifiuto(dto: RisoluzioneConfilittiNoleggio) {
    this.prenotazioniService.rifiutoAutomaticoRichieste(dto).subscribe({
      next: value => {
        if(value) {
          this.getPrenotazioni();
          this.getNumeroNoleggiDaApprovare();
          this.resettaFiltri();
          this.gestisciVisibilitaModaleAccettazioneNoleggi();
        }
      }, error: err => { console.error(err); }
    })
  }
}
