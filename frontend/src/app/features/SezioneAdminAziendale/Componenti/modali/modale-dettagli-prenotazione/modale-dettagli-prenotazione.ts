import {Component, EventEmitter, Input, Output} from '@angular/core';
import {RichiestaNoleggioDTO} from '@core/models/richiestaNoleggioDTO.models';
import {DatePipe, DecimalPipe, NgClass} from '@angular/common';

@Component({
  selector: 'app-modale-dettagli-prenotazione',
  imports: [
    NgClass,
    DatePipe,
    DecimalPipe
  ],
  templateUrl: './modale-dettagli-prenotazione.html',
  styleUrl: './modale-dettagli-prenotazione.css',
})
export class ModaleDettagliPrenotazione {

  @Input() prenotazione: RichiestaNoleggioDTO = {} as RichiestaNoleggioDTO;
  @Input() paginaVisibile = false;
  @Output() chiudiPagina = new EventEmitter<void>();
}
