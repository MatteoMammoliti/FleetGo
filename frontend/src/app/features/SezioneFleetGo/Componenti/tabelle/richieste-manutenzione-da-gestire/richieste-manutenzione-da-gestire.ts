import {Component, EventEmitter, Input, Output} from '@angular/core';
import {RichiestaManutenzioneDTO} from '@core/models/RichiestaManutenzioneDTO';
import {DatePipe, SlicePipe} from '@angular/common';
import {BottoneChiaro} from '@shared/Componenti/Bottoni/bottone-chiaro/bottone-chiaro';
import {TemplateTitoloSottotitolo} from '@shared/Componenti/IntestazionePagina/template-titolo-sottotitolo/template-titolo-sottotitolo';
import {MessaggioCardVuota} from '@shared/Componenti/Banner/messaggio-card-vuota/messaggio-card-vuota';
import {ANIMAZIONE_TABELLA} from '@shared/Animazioni/animazioneTabella';

@Component({
  selector: 'app-richieste-manutenzione-da-gestire',
  imports: [
    DatePipe,
    SlicePipe,
    BottoneChiaro,
    TemplateTitoloSottotitolo,
    MessaggioCardVuota
  ],
  templateUrl: './richieste-manutenzione-da-gestire.html',
  styleUrl: './richieste-manutenzione-da-gestire.css',
  animations: [ANIMAZIONE_TABELLA]
})
export class RichiesteManutenzioneDaGestire {
  @Input() richiesteManutenzioneDaGestire: RichiestaManutenzioneDTO[] | null = null;
  @Output() inviaRichiesta: EventEmitter<number> = new EventEmitter<number>();

  gestisciRichiesta(idManutenzione: number) {
    this.inviaRichiesta.emit(idManutenzione);
  }

}
