import { Component } from '@angular/core';
import {BannerHero} from '@features/SezionePubblica/Componenti/banner-hero/banner-hero';
import { ChiSiamo } from '../Componenti/chi-siamo/chi-siamo';
import { ComeFunziona } from '../Componenti/come-funziona/come-funziona';
import { Vantaggi } from '../Componenti/vantaggi/vantaggi';
import {FormContatto} from '@shared/Componenti/form-contatto/form-contatto';
import {SezionePubblicaService} from '@features/SezionePubblica/sezionePubblicaService';
import { BannerErrore } from '@shared/Componenti/Ui/banner-errore/banner-errore';

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
  invioInCorso = false;
  messaggioInviato = false; 
  erroreInvio = false;      
  erroreBanner = '';
  successoBanner = '';

  constructor(private service: SezionePubblicaService) {}

  gestisciInvioMessaggio(formData: FormData) {
    this.invioInCorso = true;
    this.erroreInvio = false;
    this.messaggioInviato = false;

    this.service.inviaForm(formData).subscribe({
      next: (risposta) => {
        this.invioInCorso = false;
        this.messaggioInviato = true; 
        
        this.gestisciSuccesso("Messaggio inviato con successo! Ti risponderemo presto"); 
      },
      error: (err) => {
        this.invioInCorso = false;
        this.erroreInvio = true;
        
        const msg = typeof err.error === 'string' ? err.error : "Si è verificato un errore durante l'invio";
        this.gestisciErrore(msg);
      }
    });
  }

  resetStatoForm() {
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
