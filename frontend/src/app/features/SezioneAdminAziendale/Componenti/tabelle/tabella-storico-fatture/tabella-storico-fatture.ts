import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FatturaDTO} from '@core/models/FatturaDTO.models';
import {CurrencyPipe} from '@angular/common';
import {IconaStato} from '@shared/Componenti/Ui/icona-stato/icona-stato';
import {BottonePillola} from '@shared/Componenti/Ui/bottone-pillola/bottone-pillola';
import {BottoneChiaro} from '@shared/Componenti/Ui/bottone-chiaro/bottone-chiaro';
import {MessaggioCardVuota} from '@shared/Componenti/Ui/messaggio-card-vuota/messaggio-card-vuota';

@Component({
  selector: 'app-tabella-storico-fatture',
  imports: [
    CurrencyPipe,
    IconaStato,
    BottonePillola,
    BottoneChiaro,
    MessaggioCardVuota
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
