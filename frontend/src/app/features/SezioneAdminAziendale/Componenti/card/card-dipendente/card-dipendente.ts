import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {DipendenteDTO} from '@core/models/dipendenteDTO.models';

@Component({
  selector: 'app-card-dipendente',
  imports: [],
  templateUrl: './card-dipendente.html',
  styleUrl: './card-dipendente.css',
})
export class CardDipendente implements OnInit{

  @Input() dipendente: DipendenteDTO = {} as DipendenteDTO;
  @Output() rimuoviDipendente = new EventEmitter<number | undefined>();
  @Output() apriDettagliDipendente = new EventEmitter<DipendenteDTO>();
  iniziali = "";

  ngOnInit() {
    if(this.dipendente) {
      this.iniziali = (this.dipendente.nomeUtente.charAt(0) +
        this.dipendente.cognomeUtente.charAt(0)
      ).toUpperCase();
    }
  }

  apriModaleDettagli(dipendente: DipendenteDTO) {
    if(dipendente) this.apriDettagliDipendente.emit(dipendente);
  }

  eliminaDipendente(idDipendente: number | undefined) {
    if(idDipendente) this.rimuoviDipendente.emit(idDipendente); }
}
