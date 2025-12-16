import { Component } from '@angular/core';
import {BannerHero} from '@features/SezionePubblica/banner-hero/banner-hero';
import { ChiSiamo } from '../Componenti/chi-siamo/chi-siamo';
import { ComeFunziona } from '../Componenti/come-funziona/come-funziona';
import { Contatti } from '../Componenti/contatti/contatti';
import { Vantaggi } from '../Componenti/vantaggi/vantaggi';

@Component({
  selector: 'app-landing-page',
  imports: [
    BannerHero,
    ChiSiamo,
    ComeFunziona,
    Contatti,
    Vantaggi
  ],
  templateUrl: './landing-page.html',
  styleUrl: './landing-page.css',
})
export class LandingPage {

}
