import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {DatiFiltriNuovaPrenotazione} from '@core/models/DatiFiltriNuovaPrenotazione';

@Component({
  selector: 'app-filtro-data-ora',
  imports: [
    FormsModule
  ],
  templateUrl: './filtro-data-ora.html',
  styleUrl: './filtro-data-ora.css',
})
export class FiltroDataOra implements OnInit {

  dataRitiroCompleta: string = '';
  dataConsegnaCompleta: string = '';

  minDateRitiro: string = '';
  minDateConsegna: string = '';

  erroreRitiro: boolean = false;
  msgErroreRitiro: string = '';

  erroreConsegna: boolean = false;
  msgErroreConsegna: string = '';
  
  @Output() clickRicerca = new EventEmitter<DatiFiltriNuovaPrenotazione>();

  ngOnInit() {
    const ora = new Date();

    this.minDateRitiro = this.trasformaDataInStringa(ora);
    this.minDateConsegna = this.trasformaDataInStringa(ora);

    this.dataRitiroCompleta = this.trasformaDataInStringa(ora);

    const domani = new Date();
    domani.setDate(domani.getDate() + 1);
    this.dataConsegnaCompleta = this.trasformaDataInStringa(domani);
    this.cerca();
  }

  bloccaScrittura(event: any) {
    event.preventDefault();
  }

  resetErrori() {
    this.erroreRitiro = false;
    this.erroreConsegna = false;
    this.msgErroreRitiro = '';
    this.msgErroreConsegna = '';
  }

  cerca() {
    this.resetErrori();

    if (!this.dataRitiroCompleta) {
      this.erroreRitiro = true;
      this.msgErroreRitiro = "Inserisci data ritiro";
      return;
    }

    if (!this.dataConsegnaCompleta) {
      this.erroreConsegna = true;
      this.msgErroreConsegna = "Inserisci data consegna";
      return;
    }

    const dataRitiroObj = new Date(this.dataRitiroCompleta);
    const dataConsegnaObj = new Date(this.dataConsegnaCompleta);
    const adesso = new Date();
    adesso.setSeconds(0, 0);

    if (dataRitiroObj < adesso) {
       this.erroreRitiro = true;
       this.msgErroreRitiro = "Data nel passato";
       
       const now = new Date();
       this.minDateRitiro = this.trasformaDataInStringa(now);
       this.dataRitiroCompleta = this.minDateRitiro;
       return;
    }

    if (dataConsegnaObj <= dataRitiroObj) {
       this.erroreConsegna = true;
       this.msgErroreConsegna = "Data non valida";
       return;
    }

    const pezziRitiro = this.dataRitiroCompleta.split('T');
    const pezziConsegna = this.dataConsegnaCompleta.split('T');

    const dati: DatiFiltriNuovaPrenotazione = {
      dataInizio: pezziRitiro[0],
      oraInizio: pezziRitiro[1],
      dataFine: pezziConsegna[0],
      oraFine: pezziConsegna[1]
    };

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