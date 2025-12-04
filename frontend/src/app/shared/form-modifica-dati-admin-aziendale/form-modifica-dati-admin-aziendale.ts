import {Component, inject} from '@angular/core';
import {FormBackground} from "@shared/form-background/form-background";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {validazione} from '@shared/validation/validazione';
import {ModificaDatiService} from '@core/services/ServiceSezioneAdminAziendale/modifica-dati-service';
import {ModificaDatiUtenteDTO} from '@models/ModificaDatiUtenteDTO';

@Component({
  selector: 'app-form-modifica-dati-admin-aziendale',
    imports: [
        FormBackground,
        ReactiveFormsModule,
        FormsModule
    ],
  templateUrl: './form-modifica-dati-admin-aziendale.html',
  styleUrl: './form-modifica-dati-admin-aziendale.css',
})
export class FormModificaDatiAdminAziendale {

  private service: ModificaDatiService = inject(ModificaDatiService);
  private validator: validazione = inject(validazione);

  datiCorrenti: ModificaDatiUtenteDTO = {} as ModificaDatiUtenteDTO;

  constructor() {}

  nome: string | null = '';
  cognome: string | null = '';
  data: string | null = "";
  email: string | null = '';
  nomeAzienda: string | null = '';
  partitaIva: string | null = '';
  sedeAzienda: string | null = '';

  errore = '';
  mappaErrori= {
    nome: false,
    cognome: false,
    data: false,
    email :false,
    nomeAzienda: false,
    sedeAzienda:false,
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
      data: false,
      email : false,
      nomeAzienda: false,
      sedeAzienda: false,
      partitaIva: false
    };
  }

  caricaDatiEsistenti() {

    this.service.getDati().subscribe({
      next: (datiRicevuti) => {

        console.log("Dati arrivati dal backend:", datiRicevuti);

        if(datiRicevuti) {
          this.nome = datiRicevuti.nome;
          this.cognome = datiRicevuti.cognome;
          this.data = datiRicevuti.data;
          this.email = datiRicevuti.email;
          this.nomeAzienda = datiRicevuti.nomeAzienda;
          this.partitaIva = datiRicevuti.pIva;
          this.sedeAzienda = datiRicevuti.sedeAzienda;
          this.datiCorrenti = datiRicevuti;
        }
      },
      error: (err) => {
        console.error('Errore nel caricamento dati', err);
        this.errore = 'Impossibile caricare i dati utente';
      }
    });
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
      this.service.modificaDati(datiAggiornati).subscribe({
        next: (res) => {
          if(res) {
            this.caricaDatiEsistenti();
          }
        },
        error: (err) => {
          console.log('Errore durante la modifica dati', err);
        }
      });
    }
  }
}
