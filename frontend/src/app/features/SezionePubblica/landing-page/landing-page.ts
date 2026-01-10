import {Component} from '@angular/core';
import {BannerHero} from '@features/SezionePubblica/Componenti/banner-hero/banner-hero';
import {ChiSiamo} from '../Componenti/chi-siamo/chi-siamo';
import {ComeFunziona} from '../Componenti/come-funziona/come-funziona';
import {Vantaggi} from '../Componenti/vantaggi/vantaggi';
import {FormContatto} from '@shared/Componenti/form-contatto/form-contatto';
import {SezionePubblicaService} from '@features/SezionePubblica/sezionePubblicaService';

@Component({
  selector: 'app-landing-page',
  imports: [
    BannerHero,
    ChiSiamo,
    ComeFunziona,
    Vantaggi,
    FormContatto
  ],
  templateUrl: './landing-page.html',
  styleUrl: './landing-page.css',
})

export class LandingPage {

  constructor(private sezionePubblicaService: SezionePubblicaService) {
  }

  invioInCorso = false;

  invia(formData: FormData) {

    this.invioInCorso = true;

    this.sezionePubblicaService.inviaForm(formData).subscribe({
      next: data => {

        setTimeout(() => {
          this.invioInCorso = false;
        }, 2000)

      }, error: error => {
        console.log(error);

        setTimeout(() => {
          this.invioInCorso = false;
        }, 2000)
      }
    })
  }
}
