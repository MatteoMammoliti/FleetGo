import {Component, OnInit} from '@angular/core';
import {ModaleDettagliPrenotazione} from '@features/SezioneAdminAziendale/Componenti/modale-dettagli-prenotazione/modale-dettagli-prenotazione';
import {TabellaPrenotazioni} from '@features/SezioneAdminAziendale/Componenti/tabella-prenotazioni/tabella-prenotazioni';
import {RichiestaNoleggioDTO} from '@core/models/richiestaNoleggioDTO.models';
import {PrenotazioniService} from '@features/SezioneAdminAziendale/ServiceSezioneAdminAziendale/prenotazioni-service';

@Component({
  selector: 'app-prenotazioni',
  imports: [
    ModaleDettagliPrenotazione,
    TabellaPrenotazioni
  ],
  templateUrl: './prenotazioni.html',
  styleUrl: './prenotazioni.css',
})
export class Prenotazioni implements OnInit{

  constructor(private prenotazioniService:PrenotazioniService) {}

  richiesteNoleggio: RichiestaNoleggioDTO[] = [];
  modaleDettaglioAperto = false;
  dettaglioDellaPrenotazione = {} as RichiestaNoleggioDTO;

  ngOnInit() {
    this.getPrenotazioni()
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
}
