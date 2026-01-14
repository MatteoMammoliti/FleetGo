import {Component, EventEmitter, Input, OnChanges, Output} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {validazione} from '@core/utils/validazione';
import {ModificaDatiUtenteDTO} from '@core/models/ModificaDatiUtenteDTO';
import {LuogoDTO} from '@core/models/LuogoDTO';
import {InputChecked} from '@shared/Componenti/Input/input-checked/input-checked';

@Component({
  selector: 'app-form-modifica-dati-admin-aziendale',
  imports: [
    ReactiveFormsModule,
    FormsModule,
    InputChecked,
  ],
  templateUrl: './form-modifica-dati-admin-aziendale.html',
  styleUrl: './form-modifica-dati-admin-aziendale.css',
})

export class FormModificaDatiAdminAziendale implements OnChanges {

  constructor(private validator: validazione) {
  }

  @Input() datiCorrenti: ModificaDatiUtenteDTO = {} as ModificaDatiUtenteDTO;
  @Input() luoghi: LuogoDTO[] = [];
  @Input() luoghiCorrenti: LuogoDTO[] = [];

  @Output() richiestaModifica = new EventEmitter<ModificaDatiUtenteDTO>();

  nome: string | null = '';
  cognome: string | null = '';
  data: string | null = "";
  email: string | null = '';
  nomeAzienda: string | null = '';
  partitaIva: string | null = '';
  sedeAzienda: string | null | undefined = '';

  errore = '';

  mappaErrori = {
    nome: false,
    cognome: false,
    data: false,
    email: false,
    nomeAzienda: false,
    partitaIva: false
  };

  ngOnChanges() {
    this.caricaDatiEsistenti();
  }

  caricaDatiEsistenti() {
    if (this.datiCorrenti) {
      this.nome = this.datiCorrenti.nome;
      this.cognome = this.datiCorrenti.cognome;
      this.data = this.datiCorrenti.data;
      this.email = this.datiCorrenti.email;
      this.nomeAzienda = this.datiCorrenti.nomeAzienda;
      this.partitaIva = this.datiCorrenti.pIva;
      this.sedeAzienda = this.datiCorrenti.sedeAzienda;
    }
  }

  reset() {
    this.errore = '';
    this.mappaErrori = {
      nome: false,
      cognome: false,
      data: false,
      email: false,
      nomeAzienda: false,
      partitaIva: false
    };
  }

  onSalvaModifiche() {
    this.reset();

    if (!this.nome || !this.cognome || !this.data || !this.email || !this.nomeAzienda || !this.sedeAzienda || !this.partitaIva) {
      this.errore = "Campi vuoti non permessi";
      if (!this.nome) this.mappaErrori.nome = true;
      if (!this.cognome) this.mappaErrori.cognome = true;
      if (!this.data) this.mappaErrori.data = true;
      if (!this.email) this.mappaErrori.email = true;
      if (!this.nomeAzienda) this.mappaErrori.nomeAzienda = true;
      if (!this.partitaIva) this.mappaErrori.partitaIva = true;
      return;
    }

    if (!this.validator.checkNome(this.nome)) {
      this.errore = "Nome non valido";
      this.mappaErrori.nome = true;
      return;
    }

    if (!this.validator.checkCognome(this.cognome)) {
      this.errore = "Cognome non valido";
      this.mappaErrori.cognome = true;
      return;
    }

    if (!this.validator.checkDataNascita(this.data)) {
      this.errore = "Data di nascita non valida";
      this.mappaErrori.data = true;
      return;
    }

    if (!this.validator.checkEmail(this.email)) {
      this.errore = "Email non valida";
      this.mappaErrori.email = true;
      return;
    }

    if (!this.validator.checkPartitaIva(this.partitaIva)) {
      this.errore = "Partita Iva non valida";
      this.mappaErrori.partitaIva = true;
      return;
    }

    const datiAggiornati: ModificaDatiUtenteDTO = {
      nome: this.datiCorrenti.nome != this.nome ? this.nome : null,
      cognome: this.datiCorrenti.cognome != this.cognome ? this.cognome : null,
      data: this.datiCorrenti.data != this.data ? this.data : null,
      email: this.datiCorrenti.email != this.email ? this.email : null,
      nomeAzienda: this.datiCorrenti.nomeAzienda != this.nomeAzienda ? this.nomeAzienda : null,
      sedeAzienda: null,
      pIva: this.datiCorrenti.pIva != this.partitaIva ? this.partitaIva : null,
    };

    const ciSonoModifiche = Object.values(datiAggiornati).some(val => val !== null);

    if (ciSonoModifiche) {
      console.log(datiAggiornati);
      this.richiestaModifica.emit(datiAggiornati);
    }
  }
}
