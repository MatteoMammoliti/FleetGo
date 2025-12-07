import {Component, inject, Output, EventEmitter} from '@angular/core';
import {FormBackground} from "@shared/form-background/form-background";
import {FormsModule} from "@angular/forms";
import {validazione} from '@shared/validation/validazione';
import {AziendeAffiliateService} from '@core/services/ServiceSezioneFleetGo/aziende-affiliate-service';
import {AdminAziendaleDTO} from '@models/adminAziendaleDTO.models';
import {AziendaDTO} from '@models/aziendaDTO';
import {AggiuntaNuovoAdminDTO} from '@models/aggiuntaNuovoAdmin.models';

@Component({
  selector: 'app-form-aggiungi-admin-azienda',
    imports: [
        FormBackground,
        FormsModule
    ],
  templateUrl: './form-aggiungi-admin-azienda.html',
  styleUrl: './form-aggiungi-admin-azienda.css',
})

export class FormAggiungiAdminAzienda {

  @Output() aziendaAggiunta = new EventEmitter<AggiuntaNuovoAdminDTO>();
  private validatore = inject(validazione);
  private aziendeService = inject(AziendeAffiliateService);

  nome = '';
  cognome = '';
  dataNascita = '';
  email = '';
  password = '';
  nomeAzienda = '';
  partitaIva = '';
  sedeLegale = '';
  errore = '';

  mappaErrori = {
    nome: false,
    cognome: false,
    email: false,
    dataNascita: false,
    password: false,
    nomeAzienda: false,
    partitaIva: false,
    sedeLegale: false,
  };

  mod:AggiuntaNuovoAdminDTO = {} as AggiuntaNuovoAdminDTO;

  onSubmit() {
    this.reset();

    if (!this.nome || !this.cognome || !this.email || !this.password || !this.nomeAzienda || !this.partitaIva || !this.sedeLegale) {
      this.errore = "Riempi tutti i campi";
      this.mappaErrori.nome = this.nome == '';
      this.mappaErrori.cognome = this.cognome == '';
      this.mappaErrori.email = this.email == '';
      this.mappaErrori.password = this.password == '';
      this.mappaErrori.nomeAzienda = this.nomeAzienda == '';
      this.mappaErrori.partitaIva = this.partitaIva == '';
      this.mappaErrori.sedeLegale = this.sedeLegale == '';
      return;
    }

    if(!this.validatore.checkDataNascita(this.dataNascita)){
      this.errore='Data di nascita non valida';
      this.mappaErrori.dataNascita=true;
      return;
    }

    if (!this.validatore.checkPassword(this.password)) {
      this.errore = 'Password non valida';
      this.mappaErrori.password = true;
      return;
    }

    if (!this.validatore.checkEmail(this.email)) {
      this.errore = 'Email non valida';
      this.mappaErrori.email = true;
      return;
    }

    if (!this.validatore.checkPartitaIva(this.partitaIva)) {
      this.errore = 'Partita IVA non valida';
      this.mappaErrori.partitaIva = true;
      return;
    }

    const adminAziendale: AdminAziendaleDTO = {
      nomeUtente: this.nome,
      cognomeUtente: this.cognome,
      dataNascitaUtente: this.dataNascita,
      email: this.email,
      password: this.password,
      tipoUtente: 'AdminAziendale'
    }

    const azienda: AziendaDTO = {
      nomeAzienda: this.nomeAzienda,
      pIva: this.partitaIva,
      sedeAzienda: this.sedeLegale

    }
    this.mod.adminAziendale=adminAziendale;
    this.mod.azienda=azienda;



    console.log("Form valido, invio i dati...");





    this.aziendaAggiunta.emit(this.mod);

  }

  reset() {
    this.errore = '';
    this.mappaErrori = {
      nome: false,
      cognome: false,
      dataNascita: false,
      email: false,
      password: false,
      nomeAzienda: false,
      partitaIva: false,
      sedeLegale: false,
    };
  }

  pulisciForm() {
    this.nome = '';
    this.cognome = '';
    this.dataNascita = '';
    this.email = '';
    this.password = '';
    this.nomeAzienda = '';
    this.partitaIva = '';
    this.sedeLegale = '';
    this.reset();
  }
}
