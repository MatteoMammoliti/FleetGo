import {Component, EventEmitter, Input, Output} from '@angular/core';
import {AziendaDTO} from '@core/models/aziendaDTO';

@Component({
  selector: 'app-tabella-aziende',
  standalone: true,
  imports: [],
  templateUrl: './tabella-aziende.html',
  styleUrl: './tabella-aziende.css'
})
export class TabellaAziendeComponent {

  @Input() listaAziende:AziendaDTO[] = [];
  @Output() richiediEliminazione = new EventEmitter<number>();


  elimina(idAdmin: number | undefined) {
    console.log("sono in elimina figlio" + idAdmin);
    this.richiediEliminazione.emit(idAdmin);
  }
}
