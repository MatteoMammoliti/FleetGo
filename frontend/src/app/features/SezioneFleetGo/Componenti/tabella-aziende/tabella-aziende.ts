import {Component, EventEmitter, Input, Output} from '@angular/core';
import {AziendaDTO} from '@core/models/aziendaDTO';
import {CommonModule} from '@angular/common';

@Component({
  selector: 'app-tabella-aziende',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './tabella-aziende.html',
  styleUrl: './tabella-aziende.css'
})
export class TabellaAziendeComponent {

  @Input() listaAziende:AziendaDTO[] = [];
  @Output() richiediEliminazione = new EventEmitter<number>();


  elimina(idAdmin: number | undefined) {
    console.log("sono in elimina figlio" + idAdmin);
    const conferma=confirm("Eliminando questa azienda tutti i veicoli ad essa associati saranno" +
      "contrassegnati come liberi e tutti i dipendenti verranno dissociati dall'azienda, continuare? L'OPERAZIONE è IRREVERSIBILE")
    if(conferma){
      this.richiediEliminazione.emit(idAdmin);
    }
  }
}
