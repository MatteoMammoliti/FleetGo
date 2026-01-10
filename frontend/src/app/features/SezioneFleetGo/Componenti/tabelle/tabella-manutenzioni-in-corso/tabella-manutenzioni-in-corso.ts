import {Component, EventEmitter, Input, Output} from '@angular/core';
import {RichiestaManutenzioneDTO} from '@core/models/RichiestaManutenzioneDTO';
import {DatePipe} from '@angular/common';
import {TemplateTitoloSottotitolo} from '@shared/Componenti/IntestazionePagina/template-titolo-sottotitolo/template-titolo-sottotitolo';
import {MessaggioCardVuota} from '@shared/Componenti/Banner/messaggio-card-vuota/messaggio-card-vuota';
import {BottoneChiaro} from '@shared/Componenti/Bottoni/bottone-chiaro/bottone-chiaro';
import {ANIMAZIONE_TABELLA} from '@shared/Animazioni/animazioneTabella';

@Component({
  selector: 'app-tabella-manutenzioni-in-corso',
  imports: [
    DatePipe,
    TemplateTitoloSottotitolo,
    MessaggioCardVuota,
    BottoneChiaro
  ],
  templateUrl: './tabella-manutenzioni-in-corso.html',
  styleUrl: './tabella-manutenzioni-in-corso.css',
  animations: [ANIMAZIONE_TABELLA]
})
export class TabellaManutenzioniInCorso {

  @Input() listaRichieste: RichiestaManutenzioneDTO[] | null = null
  @Output() chiudiRichiesta = new EventEmitter<number>()

  onConcludi(idRichiesta: number) {
    this.chiudiRichiesta.emit(idRichiesta)
  }

}
