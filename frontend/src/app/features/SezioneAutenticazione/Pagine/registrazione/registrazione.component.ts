import {Component} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {AuthService} from '@core/auth/auth-service';
import {DipendenteDTO} from '@core/models/DipendenteDTO';
import {Router, RouterLink} from '@angular/router';
import {validazione} from '@core/utils/validazione';
import {InputChecked} from '@shared/Componenti/Input/input-checked/input-checked';
import {BannerErrore} from '@shared/Componenti/Banner/banner-errore/banner-errore';

@Component({
  selector: 'app-registration',
  standalone: true,
  imports: [FormsModule, RouterLink, InputChecked, BannerErrore],
  templateUrl: './registrazione.component.html',
  styleUrl: './registrazione.component.css',
})

export class RegistrazioneComponent {
  constructor(private authService: AuthService, private router: Router, private validator:validazione) {
  }

  nome = '';
  cognome = '';
  email = '';
  password = '';
  ripetiPassword = '';
  datanascita = '';
  patente: any = null;



  mappaErrori = {
    nome: false,
    cognome: false,
    email: false,
    password: false,
    ripetiPassword: false,
    datanascita: false,
    patente: false
  };

  errore = "";

  erroreBanner = "";
  successoBanner = "";




  onFileSelected(event: any) {
    this.patente = event.target.files[0];
  }

  onRegistrazione() {
    this.errore = "";
    this.mappaErrori = {
      nome: false,
      cognome: false,
      email: false,
      password: false,
      ripetiPassword: false,
      datanascita: false,
      patente: false
    };


    if (this.patente == null || this.nome == '' || this.cognome == '' || this.datanascita == '' || this.email == '' || this.password == '' || this.ripetiPassword == '') {
      this.errore="Compila tutti i campi!"
      this.mappaErrori.patente = this.patente == null;
      this.mappaErrori.nome = this.nome == '';
      this.mappaErrori.cognome = this.cognome == '';
      this.mappaErrori.datanascita = this.datanascita == '';
      this.mappaErrori.email = this.email == '';
      this.mappaErrori.password = this.password == '';
      this.mappaErrori.ripetiPassword = this.ripetiPassword == '';
      return;
    } else {
      this.errore = "";
    }

    if (!this.validator.checkNome(this.nome)) {
      this.mappaErrori.nome = true;
      return;
    } else {
      this.errore = "";
    }

    if (!this.validator.checkCognome(this.cognome)) {
      this.mappaErrori.cognome = true;
      return;
    } else {
      this.errore = "";
    }

    if (!this.validator.checkDataNascita(this.datanascita)) {
      this.errore = "Data di nascita non valida";
      this.mappaErrori.datanascita = true;
      return;
    } else {
      this.errore = "";
    }

    if (this.password !== this.ripetiPassword) {
      this.errore = "Le password non coincidono";
      this.mappaErrori.password = true;
      this.mappaErrori.ripetiPassword = true;
      return
    } else {
      this.errore = "";
    }


    if (!this.validator.checkEmail(this.email) || !this.validator.checkPassword(this.password)) {
      if (!this.validator.checkEmail(this.email)) {
        this.errore = "Email non valida";
        this.mappaErrori.email = true;
      } else {
        this.errore = "";
      }
      if (!this.validator.checkPassword(this.password)) {
        this.errore = "Password non valida, usa almeno 8 caratteri, una maiuscola, un numero e un simbolo.";
        this.mappaErrori.password = true;
        this.mappaErrori.ripetiPassword = true;
      } else {
        this.errore = "";
      }
      return;
    }

    const user: DipendenteDTO = {
      nomeUtente: this.nome,
      cognomeUtente: this.cognome,
      email: this.email.toLowerCase(),
      password: this.password,
      dataNascitaUtente: this.datanascita,
      tipoUtente: 'Dipendente'
    };

    this.authService.registrazione(user, this.patente).subscribe({
      next: (response) => {
        this.router.navigate(['/login']);
        this.gestisciSuccesso("Registrazione avvenuta con successo!")
0      },
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
