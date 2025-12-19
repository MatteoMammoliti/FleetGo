import {Component, EventEmitter, Input, Output} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {DatiFiltriNuovaPrenotazione} from '@core/models/DatiFiltriNuovaPrenotazione';


@Component({
  selector: 'app-filtri-ricerca',
  imports: [
    FormsModule
  ],
  templateUrl: './filtri-ricerca.html',
  styleUrl: './filtri-ricerca.css',
})
export class FiltriRicerca {
  dataConsegnaCompleta:string='';
  dataRitiroCompleta:string='';
  @Output() clickRicerca=new EventEmitter<DatiFiltriNuovaPrenotazione>()

  cerca() {
    if (!this.dataConsegnaCompleta || !this.dataRitiroCompleta) {
      alert("Compila le date!");
      return;
    }
    const pezziRitiro = this.dataRitiroCompleta.split('T');
    const dataRitiro = pezziRitiro[0];
    const oraRitiro = pezziRitiro[1];
    const pezziConsegna = this.dataConsegnaCompleta.split('T');
    const dataConsegna = pezziConsegna[0];
    const oraConsegna = pezziConsegna[1];

    const dati:DatiFiltriNuovaPrenotazione={
      dataInizio:dataRitiro,
      oraInizio:oraRitiro,
      dataFine:dataConsegna,
      oraFine:oraConsegna
    }
    this.clickRicerca.emit(dati);
    }

}
