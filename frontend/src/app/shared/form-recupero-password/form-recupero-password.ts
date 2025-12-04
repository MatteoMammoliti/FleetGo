import {Component, inject} from '@angular/core';
import {ReactiveFormsModule} from '@angular/forms'; // Mantenuto per FormsModule, che è dentro ReactiveFormsModule
import {HttpClient} from '@angular/common/http';
import {validazione} from '@shared/validation/validazione';
import {RouterLink} from '@angular/router';
import { CommonModule } from '@angular/common';
import {FormsModule} from "@angular/forms";
import {AuthService} from '@core/services/auth-service';

@Component({
  selector: 'app-form-recupero-password',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule, // Usiamo FormsModule per ngModel
    RouterLink
  ],
  templateUrl: './form-recupero-password.html',
  styleUrl: './form-recupero-password.css',
})
export class FormRecuperoPassword {

  private http = inject(HttpClient);
  private validatore = inject(validazione);
  private service: AuthService = inject(AuthService);

  email = '';
  otp = '';
  nuovaPassword = '';
  confermaPassword = '';

  step: number = 1;
  loading: boolean = false;
  errore: string = '';
  messaggio: string = '';

  onSubmit() {
    this.reset();

    if (this.step === 1) {
      this.inviaRichiestaOtp();
    } else if (this.step === 2) {
      this.cambiaPassword();
    }
  }

  inviaRichiestaOtp() {

    if(!this.email) {
      this.errore = "campi vuoti non permessi";
      return;
    }

    if (!this.validatore.checkEmail(this.email)) {
      this.errore = "Inserisci una email valida.";
      return;
    }

    this.service.invioOTP(this.email).subscribe({
      next: (res) => {
        this.step = 2;
        this.messaggio = `Abbiamo inviato un codice a ${this.email}`;
      },
      error: (err) => {
        this.errore = err.error?.message || "L'email non è stata trovata o si è verificato un errore.";
      }
    });
  }

  cambiaPassword() {
    if (!this.nuovaPassword || !this.validatore.checkPassword(this.nuovaPassword)) {
      this.errore = "La password non è valida.";
      return;
    }

    if(!this.otp) {
      this.errore = "campi vuoti non permessi";
      return;
    }

    if (this.nuovaPassword !== this.confermaPassword) {
      this.errore = "Le password non coincidono.";
      return;
    }

    this.service.cambioPassword(this.email, this.otp, this.nuovaPassword).subscribe({
      next: (res) => {
        this.step = 3;
      },
      error: (err) => {
        this.errore = "Impossibile cambiare la password. Riprova la procedura.";
      }
    });
  }

  reset() {
    this.errore = '';
    this.messaggio = '';
  }
}
