import {Component, EventEmitter, Input, OnDestroy, OnInit, Output} from '@angular/core';
import {OffertaDTO} from '@core/models/offertaDTO.models';

@Component({
  selector: 'app-carosello-offerte',
  imports: [],
  templateUrl: './carosello-offerte.html',
  styleUrl: './carosello-offerte.css',
})
export class CaroselloOfferte implements OnInit, OnDestroy {

  @Input() offerte: OffertaDTO[] = [];
  @Output() offertaSelezionata = new EventEmitter<OffertaDTO>();

  indiceCorrente = 0;
  intervalloDiCambioCarosello: any;

  ngOnInit() { this.startRotation(); }

  ngOnDestroy() { this.stopRotation(); }

  startRotation() {
    this.intervalloDiCambioCarosello = setInterval(() => {
      this.avanzaCarosello();
    }, 5000);
  }

  stopRotation() {
    if (this.intervalloDiCambioCarosello) clearInterval(this.intervalloDiCambioCarosello);
  }

  avanzaCarosello() {
    if(this.offerte.length > 0) {
      this.indiceCorrente = (this.indiceCorrente + 1) % this.offerte.length;
    }
  }

  arretraCarosello() {
    if(this.offerte.length > 0) {
      this.indiceCorrente = (this.indiceCorrente - 1 + this.offerte.length) % this.offerte.length;
    }
  }

  vaiAdOffertaSpecifica(indice: number) {
    this.indiceCorrente = indice;
    this.stopRotation();
    this.startRotation();
  }

  richiediDettagli(offertaSelezionata: OffertaDTO){
    this.offertaSelezionata.emit(offertaSelezionata);
  }
}
