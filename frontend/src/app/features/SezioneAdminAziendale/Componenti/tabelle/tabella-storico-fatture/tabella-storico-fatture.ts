import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FatturaDTO} from '@core/models/FatturaDTO.models';
import {CurrencyPipe} from '@angular/common';
import {IconaStato} from '@shared/Componenti/Ui/icona-stato/icona-stato';
import {BottonePillola} from '@shared/Componenti/Ui/bottone-pillola/bottone-pillola';
import {BottoneChiaro} from '@shared/Componenti/Ui/bottone-chiaro/bottone-chiaro';
import {MessaggioCardVuota} from '@shared/Componenti/Ui/messaggio-card-vuota/messaggio-card-vuota';
import {ANIMAZIONE_TABELLA} from '@shared/Animazioni/animazioneTabella';


@Component({
  selector: 'app-tabella-storico-fatture',
  imports: [
    CurrencyPipe,
    IconaStato,
    BottoneChiaro,
    MessaggioCardVuota,
  ],
  templateUrl: './tabella-storico-fatture.html',
  styleUrl: './tabella-storico-fatture.css',
  animations: [ANIMAZIONE_TABELLA]
})
export class TabellaStoricoFatture {

  @Input() fattureEmesse: FatturaDTO[]|null = null;
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
