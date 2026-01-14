import {Component} from '@angular/core';
import {BannerHero} from '@features/SezionePubblica/Componenti/banner-hero/banner-hero';
import {ChiSiamo} from '../Componenti/chi-siamo/chi-siamo';
import {ComeFunziona} from '../Componenti/come-funziona/come-funziona';
import {Vantaggi} from '../Componenti/vantaggi/vantaggi';
import {FormContatto} from '@shared/Componenti/form-contatto/form-contatto';
import {SezionePubblicaService} from '@features/SezionePubblica/sezionePubblicaService';
import { BannerErrore } from '@shared/Componenti/Banner/banner-errore/banner-errore';

@Component({
  selector: 'app-landing-page',
  imports: [
    BannerHero,
    ChiSiamo,
    ComeFunziona,
    Vantaggi,
    FormContatto,
    BannerErrore
  ],
  templateUrl: './landing-page.html',
  styleUrl: './landing-page.css',
})

export class LandingPage {

  constructor(private sezionePubblicaService: SezionePubblicaService) {
  }

  invioInCorso = false;
  erroreInvio = false;
  messaggioInviato = false;
  successoBanner = '';
  erroreBanner = '';

  invia(formData: FormData) {
    this.invioInCorso = true;
    this.messaggioInviato = false;
    this.erroreInvio = false;

    this.sezionePubblicaService.inviaForm(formData).subscribe({
      next: data => {
        setTimeout(() => {
          this.invioInCorso = false;
          this.messaggioInviato = true; 
          this.gestisciSuccesso('Il tuo messaggio è stato inviato correttamente.');
        }, 500);
      },
      error: error => {
        console.log(error);
        setTimeout(() => {
          this.invioInCorso = false;
          this.erroreInvio = true; 
          this.gestisciErrore('Si è verificato un errore durante l\'invio del messaggio.');
        }, 500);
      }
    });
  }

  resetStato() {
    this.messaggioInviato = false;
    this.erroreInvio = false;
    this.invioInCorso = false;
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