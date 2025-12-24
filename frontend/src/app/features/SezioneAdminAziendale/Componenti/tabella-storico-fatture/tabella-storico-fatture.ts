import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FatturaDTO} from '@core/models/FatturaDTO.models';
import {CurrencyPipe} from '@angular/common';

@Component({
  selector: 'app-tabella-storico-fatture',
  imports: [
    CurrencyPipe
  ],
  templateUrl: './tabella-storico-fatture.html',
  styleUrl: './tabella-storico-fatture.css',
})
export class TabellaStoricoFatture {

  @Input() fattureEmesse: FatturaDTO[] = [];
  @Output() scaricaFattura = new EventEmitter<number>();
  @Output() pagaFattura = new EventEmitter<number>();

  getNomeMese(numeroMese: number) {
    const mesi = ["Gennaio", "Febbraio",
      "Marzo", "Aprile", "Maggio",
      "Giugno", "Luglio", "Agosto",
      "Settembre", "Ottobre", "Novembre", "Dicembre"];

    return mesi[numeroMese - 1];
  }
}
