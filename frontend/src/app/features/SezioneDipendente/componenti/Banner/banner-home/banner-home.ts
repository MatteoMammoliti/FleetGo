import {Component, EventEmitter, Input, Output} from '@angular/core';

@Component({
  selector: 'app-banner-home',
  imports: [],
  templateUrl: './banner-home.html',
  styleUrl: './banner-home.css',
})
export class BannerHome {

  @Input() nomeDipendente: string = "";
  @Output() clickPrenotaVeicolo = new EventEmitter();
  @Output() clickMiePrenotazioni = new EventEmitter();

}
