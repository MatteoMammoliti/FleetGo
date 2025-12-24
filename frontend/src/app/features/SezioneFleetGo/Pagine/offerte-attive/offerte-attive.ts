import {Component, OnInit} from '@angular/core';
import {CardOfferta} from '@features/SezioneFleetGo/Componenti/card-offerta/card-offerta';
import {OffertaDTO} from '@core/models/offertaDTO.models';
import {OfferteAttiveService} from '@features/SezioneFleetGo/ServiceSezioneFleetGo/offerte-attive-service';
import {ModaleAggiuntaOfferta} from '@features/SezioneFleetGo/Componenti/modale-aggiunta-offerta/modale-aggiunta-offerta';

@Component({
  selector: 'app-offerte-attive',
  imports: [
    CardOfferta,
    ModaleAggiuntaOfferta
  ],
  templateUrl: './offerte-attive.html',
  styleUrl: './offerte-attive.css',
})

export class OfferteAttive implements OnInit {

  constructor(private offerteService: OfferteAttiveService ) {}

  offerteAttive: OffertaDTO[] = [];
  apriModale = false;

  ngOnInit() { this.getOfferteAttive() }

  apriPaginaInserimentoOfferta() { this.apriModale = true; }
  chiudiPaginaInserimentoOfferta() { this.apriModale = false; }

  salvaOfferta(formData: FormData) {
    this.offerteService.inserisciNuovaOfferta(formData).subscribe({
      next: value =>{
        console.log("offerta aggiunta correttamente")
        this.chiudiPaginaInserimentoOfferta();
        this.getOfferteAttive();
      }, error: err => {
        console.error(err);
      }
    })
  }

  onRimuoviOfferta(idOfferta: number) {
    this.offerteService.eliminaOfferta(idOfferta).subscribe({
      next: value => {
        console.log("offerta eliminata correttamente")
        this.getOfferteAttive();
      }, error: err => {
        console.error(err);
      }
    })
  }

  getOfferteAttive() {
    this.offerteService.getOfferteAttive().subscribe({
      next: offerte => {
        if(offerte)
          this.offerteAttive = offerte;
      }, error: err => {
        console.error(err)
      }
    })
  }
}
