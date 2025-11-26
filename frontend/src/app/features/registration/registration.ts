import { Component } from '@angular/core';
import {FormsModule} from '@angular/forms';
import {FormAutenticazione} from '@shared/form-autenticazione/form-autenticazione';

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
  nome = '';
  email = '';
  password = '';
  datanascita = '';
  patente: any = null;

  onFileSelected(event: any) {
    this.patente = event.target.files[0];
    console.log("Documento caricato:", this.patente);
  }

  stampaDati() {
    alert("\nNome: " + this.nome + "\nEmail: " + this.email + "\nData di Nascita: " + this.datanascita);
  }
}
