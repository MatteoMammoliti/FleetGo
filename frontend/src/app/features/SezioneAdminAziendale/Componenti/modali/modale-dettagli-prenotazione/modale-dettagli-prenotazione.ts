import {Component, EventEmitter, Input, Output} from '@angular/core';
import {RichiestaNoleggioDTO} from '@core/models/RichiestaNoleggioDTO';
import {DatePipe, DecimalPipe} from '@angular/common';
import {TemplateFinestraModale} from '@shared/Componenti/Modali/template-finestra-modale/template-finestra-modale';
import {IconaStato} from '@shared/Componenti/Banner/icona-stato/icona-stato';

@Component({
  selector: 'app-modale-dettagli-prenotazione',
  imports: [
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
