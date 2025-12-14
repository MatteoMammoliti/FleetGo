import { Component } from '@angular/core';
import {BannerHero} from '@features/SezionePubblica/banner-hero/banner-hero';

@Component({
  selector: 'app-landing-page',
  imports: [
    BannerHero
  ],
  templateUrl: './landing-page.html',
  styleUrl: './landing-page.css',
})
export class LandingPage {

}
