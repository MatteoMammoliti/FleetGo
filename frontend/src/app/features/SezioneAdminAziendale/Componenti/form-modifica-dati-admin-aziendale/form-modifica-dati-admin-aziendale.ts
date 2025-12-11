import {Component, EventEmitter, inject, Input, Output} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {validazione} from '@core/utils/validazione';
import {ModificaDatiService} from '@features/SezioneAdminAziendale/ServiceSezioneAdminAziendale/modifica-dati-service';
import {ModificaDatiUtenteDTO} from '@core/models/ModificaDatiUtenteDTO';

@Component({
  selector: 'app-form-modifica-dati-admin-aziendale',
    imports: [
      ReactiveFormsModule,
      FormsModule
    ],
  templateUrl: './form-modifica-dati-admin-aziendale.html',
  styleUrl: './form-modifica-dati-admin-aziendale.css',
})
export class FormModificaDatiAdminAziendale {

  private service: ModificaDatiService = inject(ModificaDatiService);
  private validator: validazione = inject(validazione);

  @Input() datiCorrenti: ModificaDatiUtenteDTO = {} as ModificaDatiUtenteDTO;
  errore = '';
  @Input() erroreBackend="";

  @Output() richiestaModifica=new EventEmitter<ModificaDatiUtenteDTO>();

  constructor() {}

  nome: string | null = '';
  cognome: string | null = '';
  data: string | null = "";
  email: string | null = '';
  nomeAzienda: string | null = '';
  partitaIva: string | null = '';
  sedeAzienda: string | null = '';



  mappaErrori= {
    nome: false,
    cognome: false,
    data: false,
    email :false,
    nomeAzienda: false,
    sedeAzienda:false,
    partitaIva: false
  };


  ngOnChanges() {
    this.caricaDatiEsistenti();
  }

  caricaDatiEsistenti() {
    if(this.datiCorrenti) {
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
      email : false,
      nomeAzienda: false,
      sedeAzienda: false,
      partitaIva: false
    };
  }


  onSalvaModifiche() {
    this.reset();

    if (!this.nome || !this.cognome || !this.data || !this.email ||!this.nomeAzienda || !this.sedeAzienda || !this.partitaIva) {
      this.errore = "campi vuoti non permessi";
      if (!this.nome) this.mappaErrori.nome = true;
      if (!this.cognome) this.mappaErrori.cognome = true;
      if(!this.data) this.mappaErrori.data = true;
      if (!this.email) this.mappaErrori.email = true;
      if (!this.nomeAzienda) this.mappaErrori.nomeAzienda = true;
      if (!this.sedeAzienda) this.mappaErrori.sedeAzienda = true;
      if (!this.partitaIva) this.mappaErrori.partitaIva = true;
      return;
    }

    if(!this.validator.checkNome(this.nome)){
      this.errore = "Nome non valido";
      this.mappaErrori.nome = true;
      return;
    }

    if(!this.validator.checkCognome(this.cognome)) {
      this.errore = "Cognome non valido";
      this.mappaErrori.cognome = true;
      return;
    }

    if(!this.validator.checkDataNascita(this.data)) {
      this.errore = "Data di nascita non valida";
      this.mappaErrori.data = true;
      return;
    }

    if(!this.validator.checkEmail(this.email)){
      this.errore = "Email non valida";
      this.mappaErrori.email = true;
      return;
    }

    const datiAggiornati: ModificaDatiUtenteDTO = {
      nome: this.datiCorrenti.nome != this.nome ? this.nome : null,
      cognome: this.datiCorrenti.cognome != this.cognome ? this.cognome : null,
      data: this.datiCorrenti.data != this.data ? this.datiCorrenti.data : null,
      email: this.datiCorrenti.email != this.email ? this.email : null,
      nomeAzienda: this.datiCorrenti.nomeAzienda != this.nomeAzienda ? this.nomeAzienda : null,
      sedeAzienda: this.datiCorrenti.sedeAzienda != this.sedeAzienda ? this.sedeAzienda : null,
      pIva: this.datiCorrenti.pIva != this.partitaIva ? this.partitaIva : null,
    };

    const ciSonoModifiche = Object.values(datiAggiornati).some(val => val !== null);

    if(ciSonoModifiche) {
      this.richiestaModifica.emit(datiAggiornati);
    }
  }
}
