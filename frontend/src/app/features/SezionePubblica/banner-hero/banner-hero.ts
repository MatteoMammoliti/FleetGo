import { Component, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';


@Component({
  selector: 'app-banner-hero',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './banner-hero.html',
  styleUrl: './banner-hero.css',
})
export class BannerHero implements OnInit {

  veicoliGestiti: string = "2500+";
  prenotazioniMensili: string = "40k";

  constructor() {}

  ngOnInit(): void {
      this.caricaDati();
  }

  caricaDati() {}
}
