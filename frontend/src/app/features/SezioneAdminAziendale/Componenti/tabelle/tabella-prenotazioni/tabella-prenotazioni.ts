import {Component, EventEmitter, Input, Output} from '@angular/core';
import {RichiestaNoleggioDTO} from '@core/models/richiestaNoleggioDTO.models';
import {DatePipe} from '@angular/common';
import {IconaStato} from '@shared/Componenti/Ui/icona-stato/icona-stato';
import {ANIMAZIONE_TABELLA} from '@shared/Animazioni/animazioneTabella';
import {MessaggioCardVuota} from '@shared/Componenti/Ui/messaggio-card-vuota/messaggio-card-vuota';

@Component({
  selector: 'app-tabella-prenotazioni',
  imports: [
    DatePipe,
    IconaStato,
    MessaggioCardVuota
  ],
  templateUrl: './tabella-prenotazioni.html',
  styleUrl: './tabella-prenotazioni.css',
  animations: [ANIMAZIONE_TABELLA]
})
export class TabellaPrenotazioni {

  @Input() richiesteNoleggio: RichiestaNoleggioDTO[]|null = null;
  @Output() apriDettaglio = new EventEmitter<number>();

}
