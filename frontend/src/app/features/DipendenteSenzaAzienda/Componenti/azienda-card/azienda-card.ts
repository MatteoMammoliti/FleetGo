import {Component, EventEmitter, Input, Output} from '@angular/core';
import {ContenitoreDatiAzienda} from '@core/models/ContenitoreDatiAzienda';

@Component({
  selector: 'app-azienda-card',
  imports: [],
  templateUrl: './azienda-card.html',
  styleUrl: './azienda-card.css',
})
export class AziendaCard {

  @Input() datiAzienda: ContenitoreDatiAzienda = {} as ContenitoreDatiAzienda;
  @Input() selezionato: boolean = false;
  @Output() selezionaAzienda = new EventEmitter<ContenitoreDatiAzienda>();

}
