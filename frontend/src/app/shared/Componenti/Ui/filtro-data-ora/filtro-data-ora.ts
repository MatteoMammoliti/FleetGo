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

  dataRitiro: string = '';
  oraRitiro: string = '';
  
  dataConsegna: string = '';
  oraConsegna: string = '';

  minDateRitiro: string = '';
  minDateConsegna: string = '';

  erroreRitiro: boolean = false;
  msgErroreRitiro: string = '';
  erroreConsegna: boolean = false;
  msgErroreConsegna: string = '';
  
  @Output() clickRicerca = new EventEmitter<DatiFiltriNuovaPrenotazione>();

  ngOnInit() {
    const ora = new Date();
    this.minDateRitiro = this.formattaData(ora);
    this.minDateConsegna = this.formattaData(ora);

    this.dataRitiro = this.formattaData(ora);
    this.oraRitiro = this.formattaOra(ora);

    const domani = new Date();
    domani.setDate(domani.getDate() + 1);
    this.dataConsegna = this.formattaData(domani);
    this.oraConsegna = this.formattaOra(domani);

    this.cerca();
  }

  bloccaScrittura(event: any) {
    event.preventDefault();
  }

  resetErrori() {
    this.erroreRitiro = false;
    this.msgErroreRitiro = '';
    this.erroreConsegna = false;
    this.msgErroreConsegna = '';
    
    this.onCambioRitiro(); 
  }


  cerca() {
    // Reset errori precedenti
    this.erroreRitiro = false;
    this.erroreConsegna = false;

    if (!this.dataRitiro || !this.oraRitiro) {
      this.erroreRitiro = true;
      this.msgErroreRitiro = "Inserisci data e ora";
      return; 
    }

    if (!this.dataConsegna || !this.oraConsegna) {
      this.erroreConsegna = true;
      this.msgErroreConsegna = "Inserisci data e ora";
      return; 
    }

    const dataRitiroCompleta = new Date(`${this.dataRitiro}T${this.oraRitiro}`);
    const dataConsegnaCompleta = new Date(`${this.dataConsegna}T${this.oraConsegna}`);
    
    const adesso = new Date();
    adesso.setSeconds(0, 0);

    if (dataRitiroCompleta < adesso) {
      this.erroreRitiro = true;
      this.msgErroreRitiro = "La data deve essere futura";
      return;
    }

    if (dataConsegnaCompleta <= dataRitiroCompleta) {
       this.erroreConsegna = true;
       this.msgErroreConsegna = "Deve essere successiva al ritiro";
       return;
    }

    const dati: DatiFiltriNuovaPrenotazione = {
      dataInizio: this.dataRitiro,
      oraInizio: this.oraRitiro,
      dataFine: this.dataConsegna,
      oraFine: this.oraConsegna
    };

    this.clickRicerca.emit(dati);
  }

  onCambioRitiro() {
    if (this.dataRitiro) {
      this.minDateConsegna = this.dataRitiro;

      if (this.dataConsegna && this.dataConsegna < this.dataRitiro) {
        this.dataConsegna = this.dataRitiro;
      }
    }
  }

  private formattaData(data: Date): string {
    const anno = data.getFullYear();
    const mese = ('0' + (data.getMonth() + 1)).slice(-2);
    const giorno = ('0' + data.getDate()).slice(-2);
    return `${anno}-${mese}-${giorno}`;
  }

  private formattaOra(data: Date): string {
    const ore = ('0' + data.getHours()).slice(-2);
    const minuti = ('0' + data.getMinutes()).slice(-2);
    return `${ore}:${minuti}`;
  }
}