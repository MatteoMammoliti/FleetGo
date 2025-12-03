import { Component, OnInit, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { FormBackground } from '@shared/form-background/form-background';

@Component({
  selector: 'app-modifica-dati',
  standalone: true,
  imports: [
    FormsModule,
    FormBackground
  ],
  templateUrl: './modifica-dati.html', 
  styleUrl: './modifica-dati.css'
})
export class ModificaDatiComponent implements OnInit {

  nome = '';
  cognome = '';
  email = '';

  nomeAzienda = '';
  partitaIva = ''; 
  sedeLegale = '';

  errore = '';

  mappaErrori= {
    nome: false,
    cognome: false,
    email :false,
    nomeAzienda: false,
    sedeLegale:false,
    partitaIva: false
  };


  ngOnInit() {
    this.caricaDatiEsistenti();
  }

  reset() {
    this.errore = '';
    this.mappaErrori = {
      nome: false,  
      cognome: false,
      email : false,
      nomeAzienda: false,
      sedeLegale: false,
      partitaIva: false
    };
  }


  caricaDatiEsistenti() {
  }

  onSalvaModifiche() {
    this.reset();

    if (!this.nome || !this.cognome || !this.nomeAzienda || !this.sedeLegale) {
      this.errore = "campi vuoti non permessi";
      if (!this.nome) this.mappaErrori.nome = true;
      if (!this.cognome) this.mappaErrori.cognome = true;
      if (!this.email) this.mappaErrori.email = true;
      if (!this.nomeAzienda) this.mappaErrori.nomeAzienda = true;
      if (!this.sedeLegale) this.mappaErrori.sedeLegale = true;
      if (!this.partitaIva) this.mappaErrori.partitaIva = true;
      return;
    }

    const datiAggiornati = {
      nome: this.nome,
      cognome: this.cognome,
      nomeAzienda: this.nomeAzienda,
      sedeLegale: this.sedeLegale
    };
  }
}