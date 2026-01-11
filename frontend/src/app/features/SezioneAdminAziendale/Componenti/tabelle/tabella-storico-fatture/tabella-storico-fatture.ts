import {Component, EventEmitter, Input, Output} from '@angular/core';
import {FatturaDTO} from '@core/models/FatturaDTO';
import {CurrencyPipe} from '@angular/common';
import {IconaStato} from '@shared/Componenti/Banner/icona-stato/icona-stato';
import {BottoneChiaro} from '@shared/Componenti/Bottoni/bottone-chiaro/bottone-chiaro';
import {MessaggioCardVuota} from '@shared/Componenti/Banner/messaggio-card-vuota/messaggio-card-vuota';
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

  @Input() fattureEmesse: FatturaDTO[] | null = null;
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
