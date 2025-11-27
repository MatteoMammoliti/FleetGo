import {Component, inject} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {FormAutenticazione} from '@shared/form-autenticazione/form-autenticazione';
import {AuthService} from '@core/services/auth-service';
import {DipendenteDTO} from '@models/dipendenteDTO.models';

@Component({
  selector: 'app-registration',
  imports: [
    FormsModule,
    FormAutenticazione
  ],
  templateUrl: './registration.html',
  styleUrl: './registration.css',
})

export class Registration {
  constructor(private authService: AuthService) {}

  nome = '';
  cognome='';
  email = '';
  password = '';
  datanascita = '';
  patente: any = null;
  private user: DipendenteDTO | undefined;


  onRegistrazione() {
    this.user = {
      nomeUtente: this.nome,
      cognomeUtente: this.cognome,
      email: this.email,
      password: this.password,
      dataNascitaUtente: this.datanascita,
      tipoUtente: 'DIPENDENTE',
      patenteAccettata: false
    };
    this.authService.registrazione(this.user,this.patente);
  }
  onFileSelected(event: any) {
    this.patente = event.target.files[0];
    console.log("Documento caricato:", this.patente);
  }

  stampaDati() {
    alert("\nNome: " + this.nome + "\nEmail: " + this.email + "\nData di Nascita: " + this.datanascita);
  }
}
