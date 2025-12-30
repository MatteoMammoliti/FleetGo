import {Component, OnInit} from '@angular/core';
import {CardOfferta} from '@features/SezioneFleetGo/Componenti/card-offerta/card-offerta';
import {OffertaDTO} from '@core/models/offertaDTO.models';
import {OfferteAttiveService} from '@features/SezioneFleetGo/ServiceSezioneFleetGo/offerte-attive-service';
import {ModaleAggiuntaOfferta} from '@features/SezioneFleetGo/Componenti/modale-aggiunta-offerta/modale-aggiunta-offerta';
import { TemplateTitoloSottotitolo } from '@shared/Componenti/Ui/template-titolo-sottotitolo/template-titolo-sottotitolo';
import {BannerErrore} from "@shared/Componenti/Ui/banner-errore/banner-errore";
import {TemplateFinestraModale} from '@shared/Componenti/Ui/template-finestra-modale/template-finestra-modale';

@Component({
  selector: 'app-offerte-attive',
<<<<<<< Updated upstream
    imports: [
        CardOfferta,
        ModaleAggiuntaOfferta,
        BannerErrore
    ],
=======
  imports: [
    CardOfferta,
    ModaleAggiuntaOfferta,
    BannerErrore,
  ],
>>>>>>> Stashed changes
  templateUrl: './offerte-attive.html',
  styleUrl: './offerte-attive.css',
})

export class OfferteAttive implements OnInit {

  constructor(private offerteService: OfferteAttiveService ) {}

  offerteAttive: OffertaDTO[] = [];
  apriModale = false;
  erroreBanner="";
  successoBanner="";

  ngOnInit() { this.getOfferteAttive() }

  apriPaginaInserimentoOfferta() { this.apriModale = true; }
  chiudiPaginaInserimentoOfferta() { this.apriModale = false; }

  salvaOfferta(formData: FormData) {
    this.offerteService.inserisciNuovaOfferta(formData).subscribe({
      next: value =>{
        this.chiudiPaginaInserimentoOfferta();
        this.getOfferteAttive();
        this.gestisciSuccesso("Offerta salvata con successo");
      }, error: err => {
        this.gestisciErrore(err.error);
      }
    })
  }

  onRimuoviOfferta(idOfferta: number) {
    this.offerteService.eliminaOfferta(idOfferta).subscribe({
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
        if(offerte)
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
