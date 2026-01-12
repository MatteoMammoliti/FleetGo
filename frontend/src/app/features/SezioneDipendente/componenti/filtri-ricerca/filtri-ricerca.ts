import {Component, EventEmitter, OnInit, Output} from '@angular/core';
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

export class FiltriRicerca implements OnInit {

  dataConsegnaCompleta: string = '';
  dataRitiroCompleta: string = '';

  minDateRitiro: string = '';
  minDateConsegna: string = '';
  @Output() clickRicerca = new EventEmitter<DatiFiltriNuovaPrenotazione>()

  ngOnInit() {
    this.impostaDateDefault();
    this.cerca();
  }

  private impostaDateDefault() {
    const dataStart = new Date();

    dataStart.setMinutes(dataStart.getMinutes() + 5);

    const dataEnd = new Date(dataStart);
    dataEnd.setDate(dataStart.getDate() + 1);

    this.dataRitiroCompleta = this.trasformaDataInStringa(dataStart);
    this.dataConsegnaCompleta = this.trasformaDataInStringa(dataEnd);
  }

  bloccaScrittura(event: any) {
    event.preventDefault();
  }

  cerca() {
    if (!this.dataConsegnaCompleta || !this.dataRitiroCompleta) {
      return;
    }

    const dataRitiroCompleta = new Date(this.dataRitiroCompleta);
    const dataConsegnaCompleta = new Date(this.dataConsegnaCompleta);
    const adesso = new Date();
    adesso.setSeconds(0, 0);

    if (dataRitiroCompleta < adesso || dataConsegnaCompleta < adesso) {
      const adesso = new Date();
      this.minDateRitiro = this.trasformaDataInStringa(adesso);
      this.dataRitiroCompleta = this.minDateRitiro;
      return;
    }

    const pezziRitiro = this.dataRitiroCompleta.split('T');
    const dataRitiro = pezziRitiro[0];
    const oraRitiro = pezziRitiro[1];
    const pezziConsegna = this.dataConsegnaCompleta.split('T');
    const dataConsegna = pezziConsegna[0];
    const oraConsegna = pezziConsegna[1];

    const dati: DatiFiltriNuovaPrenotazione = {
      dataInizio: dataRitiro,
      oraInizio: oraRitiro,
      dataFine: dataConsegna,
      oraFine: oraConsegna
    }

    this.clickRicerca.emit(dati);
  }

  onCambioRitiro() {
    if (this.dataRitiroCompleta) {
      this.minDateConsegna = this.dataRitiroCompleta;

      if (this.dataConsegnaCompleta && this.dataConsegnaCompleta < this.dataRitiroCompleta) {
        this.dataConsegnaCompleta = '';
      }
    }
  }

  private trasformaDataInStringa(data: Date): string {
    const anno = data.getFullYear();
    const mese = ('0' + (data.getMonth() + 1)).slice(-2);
    const giorno = ('0' + data.getDate()).slice(-2);
    const ore = ('0' + data.getHours()).slice(-2);
    const minuti = ('0' + data.getMinutes()).slice(-2);

    return `${anno}-${mese}-${giorno}T${ore}:${minuti}`;
  }
}
