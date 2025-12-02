import {Component, inject} from '@angular/core';
import {FormBackground} from "@shared/form-background/form-background";
import {FormsModule} from "@angular/forms";
import {validazione} from '@shared/validation/validazione';
import {AziendeAffiliateService} from '@core/services/adminFleetGoService/aziende-affiliate-service';
import {AdminAziendaleDTO} from '@models/adminAziendaleDTO.models';
import {AziendaDTO} from '@models/aziendaDTO';

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
  private validatore = inject(validazione);
  private aziendeService = inject(AziendeAffiliateService);

  nome = '';
  cognome = '';
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
    password: false,
    nomeAzienda: false,
    partitaIva: false,
    sedeLegale: false,
  };

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
      email: this.email,
      password: this.password,
      tipoUtente: 'AdminAziendale'
    }

    const azienda: AziendaDTO = {
      nomeAzienda: this.nomeAzienda,
      partitaIva: this.partitaIva,
      sedeAzienda: this.sedeLegale
    }

    console.log("Form valido, invio i dati...");

    this.aziendeService.registraAzienda(adminAziendale, azienda).subscribe({
      next: (res) => {
        console.log("Azienda registrata!", res);
        this.pulisciForm();
      },
      error: (err) => {
        console.error("Errore", err);
        this.errore = "Errore durante la registrazione";
      }
    });
  }

  reset() {
    this.errore = '';
    this.mappaErrori = {
      nome: false,
      cognome: false,
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
    this.email = '';
    this.password = '';
    this.nomeAzienda = '';
    this.partitaIva = '';
    this.sedeLegale = '';
    this.reset();
  }
}
