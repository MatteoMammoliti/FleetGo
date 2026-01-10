import {Component, EventEmitter, Input, Output} from '@angular/core';
import {ContenitoreDatiAzienda} from '@core/models/ContenitoreDatiAzienda';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-richiesta-affiliazione',
  imports: [
    FormsModule
  ],
  templateUrl: './richiesta-affiliazione.html',
  styleUrl: './richiesta-affiliazione.css',
})
export class RichiestaAffiliazione {
  @Input() aziendaSelezionata: ContenitoreDatiAzienda = {} as ContenitoreDatiAzienda;
  @Output() inviaRichiesta = new EventEmitter();


  confermaRichiesta() {
    this.inviaRichiesta.emit(this.aziendaSelezionata.idAzienda);

  }
}
