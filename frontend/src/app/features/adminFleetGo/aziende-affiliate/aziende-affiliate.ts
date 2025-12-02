import { Component, inject } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { Router } from '@angular/router';
import { FormAutenticazione } from '@shared/form-autenticazione/form-autenticazione';
import { validazione } from '@shared/validation/validazione';
import { AziendeAffiliateService } from '@core/services/adminFleetGoService/aziende-affiliate-service'; 
import { TabellaAziendeComponent } from '@shared/tabella-aziende/tabella-aziende';

@Component({
  selector: 'app-aziende-affiliate',
  standalone: true, 
  imports: [
    ReactiveFormsModule,
    FormsModule,
    FormAutenticazione,
    TabellaAziendeComponent
  ],
  templateUrl: './aziende-affiliate.html',
  styleUrl: './aziende-affiliate.css',
})

export class AziendeAffiliate {
  
  private router = inject(Router);
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

    const formData = new FormData();
    formData.append('nome', this.nome);
    formData.append('cognome', this.cognome);
    formData.append('email', this.email); 
    formData.append('password', this.password);
    formData.append('nomeAzienda', this.nomeAzienda);
    formData.append('partitaIva', this.partitaIva);
    formData.append('sedeLegale', this.sedeLegale);

    console.log("Form valido, invio i dati...");

    this.aziendeService.registraAzienda(formData).subscribe({
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
