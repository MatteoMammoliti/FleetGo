import {Component, EventEmitter, Input, Output} from '@angular/core';
import {RichiestaNoleggioDTO} from '@core/models/richiestaNoleggioDTO.models';
import {DatePipe} from '@angular/common';

@Component({
  selector: 'app-tabella-prenotazioni',
  imports: [
    DatePipe
  ],
  templateUrl: './tabella-prenotazioni.html',
  styleUrl: './tabella-prenotazioni.css',
})
export class TabellaPrenotazioni {

  @Input() richiesteNoleggio: RichiestaNoleggioDTO[] = [];
  @Output() apriDettaglio = new EventEmitter<number>();
}
