import {Component, inject} from '@angular/core';
import {ReactiveFormsModule, Validators} from '@angular/forms';
import {Router, RouterLink} from '@angular/router';
import {AuthService} from '@core/auth/auth-service';
import {validazione} from '@core/utils/validazione';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule,FormsModule,RouterLink],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',
})

export class LoginComponent {
  validator = inject(validazione);

  email = '';
  password = '';

  mappaErrori = {
    email: false,
    password: false,
  };

  private router = inject(Router);
  private authService = inject(AuthService);

  errore='';

  onSubmit() {
    this.errore="";
    this.mappaErrori = {
    email: false,
    password: false,
  };

    const email = this.email;
    const password = this.password;

    if (email == '' || password == '') {
      this.errore = "Compila tutti i campi!";
      this.mappaErrori.email = email == '';
      this.mappaErrori.password = password == '';

      return;
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
        this.errore = "Password non valida";
        this.mappaErrori.password = true;
      } else {
        this.errore = "";
      }
      return;
    }

    this.errore = "";

    this.authService.login(email, password).subscribe({
      next: (response: any) => {
        this.authService.aggiornaRuoloUtenteCorrente(response.ruolo, response.idAzienda || null);

        if(response.ruolo ==='Dipendente'){

          if(response.idAzienda){
            this.router.navigate(['/dashboardDipendente'])
          }else {
            this.router.navigate(['/senza-azienda'])
          }

        }else{
          this.router.navigate([response.redirectUrl]);
        }
      },

      error: (error) => {

        if(error.status==401) {
          this.errore = "Credenziali non valide";
          return;
        }
        this.errore = "Errore di connessione";
      }
    });
  }

  recuperoPassword():any {
    this.router.navigate(['/recuperoPassword']);
  }
}
