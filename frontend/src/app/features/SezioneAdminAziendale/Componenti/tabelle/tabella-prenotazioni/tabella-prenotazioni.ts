import {Component, EventEmitter, Input, Output} from '@angular/core';
import {RichiestaNoleggioDTO} from '@core/models/richiestaNoleggioDTO.models';
import {DatePipe} from '@angular/common';
import {IconaStato} from '@shared/Componenti/Ui/icona-stato/icona-stato';

@Component({
  selector: 'app-tabella-prenotazioni',
  imports: [
    DatePipe,
    IconaStato
  ],
  templateUrl: './tabella-prenotazioni.html',
  styleUrl: './tabella-prenotazioni.css',
})
export class TabellaPrenotazioni {

  @Input() richiesteNoleggio: RichiestaNoleggioDTO[]|null = null;
  @Output() apriDettaglio = new EventEmitter<number>();

}
