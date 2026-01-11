import {Component, OnInit} from '@angular/core';
import {CardOfferta} from '@features/SezioneFleetGo/Componenti/card/card-offerta/card-offerta';
import {OffertaDTO} from '@core/models/OffertaDTO';
import {OfferteAttiveService} from '@features/SezioneFleetGo/ServiceSezioneFleetGo/offerte-attive-service';
import {
  ModaleAggiuntaOfferta
} from '@features/SezioneFleetGo/Componenti/modali/modale-aggiunta-offerta/modale-aggiunta-offerta';
import {BannerErrore} from "@shared/Componenti/Banner/banner-errore/banner-errore";
import {TemplateFinestraModale} from '@shared/Componenti/Modali/template-finestra-modale/template-finestra-modale';

@Component({
  selector: 'app-offerte-attive',
  imports: [
    CardOfferta,
    ModaleAggiuntaOfferta,
    BannerErrore,
    TemplateFinestraModale
  ],
  templateUrl: './offerte-attive.html',
  styleUrl: './offerte-attive.css',
})

export class OfferteAttive implements OnInit {

  constructor(private offerteService: OfferteAttiveService) {
  }

  offerteAttive: OffertaDTO[] = [];
  apriModale = false;
  erroreBanner = "";
  successoBanner = "";

  checkElimina = false;
  offertaDaEliminare: any = null;

  ngOnInit() {
    this.getOfferteAttive()
  }

  apriPaginaInserimentoOfferta() {
    this.apriModale = true;
  }

  chiudiPaginaInserimentoOfferta() {
    this.apriModale = false;
  }

  salvaOfferta(formData: FormData) {
    this.offerteService.inserisciNuovaOfferta(formData).subscribe({
      next: value => {
        this.chiudiPaginaInserimentoOfferta();
        this.getOfferteAttive();
        this.gestisciSuccesso("Offerta salvata con successo");
      }, error: err => {
        this.gestisciErrore(err.error);
      }
    })
  }

  onRimuoviOfferta() {
    this.offerteService.eliminaOfferta(this.offertaDaEliminare).subscribe({
      next: value => {
        this.getOfferteAttive();
        this.gestisciSuccesso("Offerta rimossa con successo");
      }, error: err => {
        this.gestisciErrore(err.error);
      }
    })
  }

  getOfferteAttive() {
    this.offerteService.getOfferteAttive().subscribe({
      next: offerte => {
        if (offerte)
          this.offerteAttive = offerte;
      }, error: err => {
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
