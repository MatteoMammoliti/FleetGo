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

  ngOnInit() {
    this.impostaMinimiDate();
    this.impostaDateDefault();
    this.cerca()
  }

  dataConsegnaCompleta:string='';
  dataRitiroCompleta:string='';

  minDateRitiro : string = '';
  minDateConsegna : string = '';
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

    impostaMinimiDate() {
      const now = new Date();
      this.minDateRitiro = now.toISOString().slice(0, 16);
      this.minDateConsegna = this.minDateRitiro;
    }

  impostaDateDefault() {
    const oggi = new Date();
    this.dataRitiroCompleta = this.formattaDataLocale(oggi);
    const domani = new Date();
    domani.setDate(domani.getDate() + 1);
    this.dataConsegnaCompleta = this.formattaDataLocale(domani);
  }
  private formattaDataLocale(data: Date): string {
    const d = new Date(data);
    d.setMinutes(d.getMinutes() - d.getTimezoneOffset());
    return d.toISOString().slice(0, 16);
  }

    onCambioRitiro() {
    if (this.dataRitiroCompleta) {
      this.minDateConsegna = this.dataRitiroCompleta;

      if (this.dataConsegnaCompleta && this.dataConsegnaCompleta < this.dataRitiroCompleta) {
        this.dataConsegnaCompleta = '';
      }
    }
  }



}
