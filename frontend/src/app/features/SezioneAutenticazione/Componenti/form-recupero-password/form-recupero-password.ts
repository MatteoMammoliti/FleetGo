import {Component, EventEmitter, Input, Output} from '@angular/core';
import {validazione} from '@core/utils/validazione';
import {RouterLink} from '@angular/router';
import {CommonModule} from '@angular/common';
import {FormsModule} from "@angular/forms";

@Component({
  selector: 'app-form-recupero-password',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    RouterLink
  ],
  templateUrl: './form-recupero-password.html',
  styleUrl: './form-recupero-password.css',
})
export class FormRecuperoPassword {
  constructor(private validatore:validazione){}

  email = '';
  otp = '';
  nuovaPassword = '';
  confermaPassword = '';

  @Output() chiamaOtp = new EventEmitter<string>();
  @Output() cambiaPass = new EventEmitter<any>();

  @Input() step: number = 1;
  @Input() errorePadre: string = '';
  @Input() messaggio: string = '';
  loading: boolean = false;
  errore: string = '';


  onSubmit() {
    this.reset();

    if (this.step === 1) {
      this.inviaRichiestaOtp();
    } else if (this.step === 2) {
      this.cambiaPassword();
    }
  }

  inviaRichiestaOtp() {

    if (!this.email) {
      this.errore = "campi vuoti non permessi";
      return;
    }

    if (!this.validatore.checkEmail(this.email)) {
      this.errore = "Inserisci una email valida.";
      return;
    }

    this.chiamaOtp.emit(this.email);
  }

  cambiaPassword() {
    if(!this.nuovaPassword || !this.confermaPassword || !this.otp){
      this.errore = "Compila tutti i campi!";
      return;
    }
    if (!this.nuovaPassword || !this.validatore.checkPassword(this.nuovaPassword)) {
      this.errore = "Password non valida, usa almeno 8 caratteri, una maiuscola, un numero e un simbolo.";
      return;
    }

    if (this.nuovaPassword !== this.confermaPassword) {
      this.errore = "Le password non coincidono.";
      return;
    }

    const dati = {
      email: this.email,
      otp: this.otp,
      password: this.nuovaPassword
    }

    this.cambiaPass.emit(dati);
  }

  reset() {
    this.errore = '';
    this.messaggio = '';
  }
}
