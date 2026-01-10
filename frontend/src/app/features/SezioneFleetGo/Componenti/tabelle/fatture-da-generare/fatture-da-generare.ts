import {Component, EventEmitter, Input, Output} from '@angular/core';
import {FatturaDaGenerareDTO} from '@core/models/FatturaDaGenerareDTO';
import {BottoneChiaro} from '@shared/Componenti/Bottoni/bottone-chiaro/bottone-chiaro';
import {MessaggioCardVuota} from '@shared/Componenti/Banner/messaggio-card-vuota/messaggio-card-vuota';
import {TemplateTitoloSottotitolo} from '@shared/Componenti/IntestazionePagina/template-titolo-sottotitolo/template-titolo-sottotitolo';
import {CurrencyPipe} from '@angular/common';
import {ANIMAZIONE_TABELLA} from '@shared/Animazioni/animazioneTabella';

@Component({
  selector: 'app-fatture-da-generare',
  imports: [
    BottoneChiaro,
    MessaggioCardVuota,
    TemplateTitoloSottotitolo,
    CurrencyPipe
  ],
  standalone: true,
  templateUrl: './fatture-da-generare.html',
  styleUrl: './fatture-da-generare.css',
  animations: [ANIMAZIONE_TABELLA]
})
export class FattureDaGenerare {
  @Input() fatture: FatturaDaGenerareDTO[] | null = null;
  @Output() generaFatturaEvent: EventEmitter<any> = new EventEmitter<any>();

  protected generaFattura(fattura: FatturaDaGenerareDTO) {
    this.generaFatturaEvent.emit(fattura);
  }
}
