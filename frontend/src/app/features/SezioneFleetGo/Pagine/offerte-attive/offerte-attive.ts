import {Component, OnInit} from '@angular/core';
import {CardOfferta} from '@features/SezioneFleetGo/Componenti/card-offerta/card-offerta';
import {OffertaDTO} from '@core/models/offertaDTO.models';
import {OfferteAttiveService} from '@features/SezioneFleetGo/ServiceSezioneFleetGo/offerte-attive-service';
import {ModaleAggiuntaOfferta} from '@features/SezioneFleetGo/Componenti/modale-aggiunta-offerta/modale-aggiunta-offerta';
import { TemplateTitoloSottotitolo } from '@shared/Componenti/Ui/template-titolo-sottotitolo/template-titolo-sottotitolo';
import {BannerErrore} from "@shared/Componenti/Ui/banner-errore/banner-errore";

@Component({
  selector: 'app-offerte-attive',
    imports: [
        CardOfferta,
        ModaleAggiuntaOfferta,
        TemplateTitoloSottotitolo,
        BannerErrore
    ],
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
        this.successoBanner = "Offerta salvata con successo"
      }, error: err => {
        this.erroreBanner=err.error;
      }
    })
  }

  onRimuoviOfferta(idOfferta: number) {
    this.offerteService.eliminaOfferta(idOfferta).subscribe({
      next: value => {
        this.getOfferteAttive();
        this.successoBanner = "Offerta rimossa con successo"
      }, error: err => {
        this.erroreBanner=err.error;
      }
    })
  }

  getOfferteAttive() {
    this.offerteService.getOfferteAttive().subscribe({
      next: offerte => {
        if(offerte)
          this.offerteAttive = offerte;
      }, error: err => {
        this.erroreBanner=err.error;
      }
    })
  }
}
