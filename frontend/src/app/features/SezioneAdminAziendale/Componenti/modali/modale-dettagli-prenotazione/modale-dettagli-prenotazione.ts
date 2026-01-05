import {Component, EventEmitter, Input, Output} from '@angular/core';
import {RichiestaNoleggioDTO} from '@core/models/richiestaNoleggioDTO.models';
import {DatePipe, DecimalPipe, NgClass} from '@angular/common';
import {TemplateFinestraModale} from '@shared/Componenti/Ui/template-finestra-modale/template-finestra-modale';
import {IconaStato} from '@shared/Componenti/Ui/icona-stato/icona-stato';

@Component({
  selector: 'app-modale-dettagli-prenotazione',
  imports: [
    NgClass,
    DatePipe,
    DecimalPipe,
    TemplateFinestraModale,
    IconaStato
  ],
  templateUrl: './modale-dettagli-prenotazione.html',
  styleUrl: './modale-dettagli-prenotazione.css',
})
export class ModaleDettagliPrenotazione {

  @Input() prenotazione: RichiestaNoleggioDTO = {} as RichiestaNoleggioDTO;
  @Input() paginaVisibile = false;
  @Output() chiudiPagina = new EventEmitter<void>();
}
