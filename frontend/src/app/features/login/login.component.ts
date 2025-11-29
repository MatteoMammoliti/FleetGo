import {Component, inject} from '@angular/core';
import {ReactiveFormsModule, Validators} from '@angular/forms';
import {Router} from '@angular/router';
import {FormAutenticazione} from '@shared/form-autenticazione/form-autenticazione';
import {AuthService} from '@core/services/auth-service';
import {validazione} from '@shared/validation/validazione'; 
import { FormsModule } from '@angular/forms';
import { DipendenteDTO } from '@models/dipendenteDTO.models';


@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule,FormAutenticazione,FormsModule],
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
    

    if (this.validator.checkEmail(this.email) == false || this.validator.checkPassword(this.password) == false) {
      if (this.validator.checkEmail(this.email) == false) {
        this.errore = "Email non valida";
        this.mappaErrori.email = true;
      } else {
        this.errore = "";
      }
      if (this.validator.checkPassword(this.password) == false) {
        this.errore = "Password non valida";
        this.mappaErrori.password = true;
      } else {
        this.errore = "";
      }
      return;
    }

    this.errore = "";


    console.log(email, password);
    

    this.authService.login(email, password).subscribe({
      next: (response: any) => {
        
        this.authService.aggiornaRuoloUtenteCorrente(response);

        switch (response) {
          case 'Dipendente':
            this.router.navigate(['/dashboardDipendente']);
            break;
          case 'AdminAziendale':
            this.router.navigate(['/dashboardAzienda']);
            break;
          case 'FleetGo':
            this.router.navigate(['/dashboardFleetGo']);
            break;
          default:
            this.router.navigate(['/errorPage']);
            break;
        }
      },
      error: (error) => {
        console.error('Errore durante il login:', error);
        this.errore = "Credenziali non valide";
      }
    });
  }
}
