import { Component } from '@angular/core';
import {FormRecuperoPassword} from '@features/SezioneAutenticazione/Componenti/form-recupero-password/form-recupero-password';
import {AuthService} from '@core/auth/auth-service';
import {validazione} from '@core/utils/validazione';
import {HttpClient} from '@angular/common/http';

@Component({
  selector: 'app-recupero-password',
  imports: [
    FormRecuperoPassword
  ],
  templateUrl: './recupero-password.html',
  styleUrl: './recupero-password.css',
})
export class RecuperoPassword {

  constructor(private service: AuthService ) {}

  stepCorrente= 1;
  errore='';
  messaggio='';

  inviaRichiestaOtp(email: string) {
    this.service.invioOTP(email).subscribe({
      next: (res) => {
        this.stepCorrente = 2;
        this.messaggio = `Abbiamo inviato un codice a ${email}`;
      },
      error: (err) => {
        this.errore = err.error?.message || "L'email non è stata trovata o si è verificato un errore.";
      }
    });
  }

  cambiaPassword(dati:any) {
    this.service.cambioPassword(dati.email, dati.otp, dati.password).subscribe({
      next: (res) => {
        this.stepCorrente = 3;
      },
      error: (err) => {
        this.errore = "Impossibile cambiare la password. Riprova la procedura.";
      }
    });
  }
}
