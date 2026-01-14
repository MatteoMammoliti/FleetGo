import {Component} from '@angular/core';
import {
  FormRecuperoPassword
} from '@features/SezioneAutenticazione/Componenti/form-recupero-password/form-recupero-password';
import {AuthService} from '@core/auth/auth-service';
import {BannerErrore} from '@shared/Componenti/Banner/banner-errore/banner-errore';

@Component({
  selector: 'app-recupero-password',
  imports: [
    FormRecuperoPassword,
    BannerErrore
  ],
  templateUrl: './recupero-password.html',
  styleUrl: './recupero-password.css',
})
export class RecuperoPassword {

  constructor(private service: AuthService) {
  }

  stepCorrente = 1;
  errore = '';
  messaggio = '';

  erroreBanner = "";
  successoBanner = "";

  inviaRichiestaOtp(email: string) {
    this.service.invioOTP(email).subscribe({
      next: (res) => {
        this.stepCorrente = 2;
        this.messaggio = `Abbiamo inviato un codice a ${email}`;
      },
      error: (err) => {
        this.gestisciErrore(err.error);
      }
    });
  }

  cambiaPassword(dati: any) {
    this.service.cambioPassword(dati.email, dati.otp, dati.password).subscribe({
      next: (res) => {
        this.stepCorrente = 3;
        this.gestisciSuccesso("Password cambiata con successo!");
      },
      error: (err) => {
        this.gestisciErrore(err.error);
      }
    });
  }

  gestisciErrore(messaggio: string) {
    this.successoBanner = '';
    this.erroreBanner = messaggio;
    setTimeout(() => this.erroreBanner = '', 5000);
  }

  gestisciSuccesso(messaggio: string) {
    this.erroreBanner = '';
    this.successoBanner = messaggio;
    setTimeout(() => this.successoBanner = '', 3000);
  }
}
