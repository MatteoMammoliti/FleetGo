import {Component} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {Router, RouterLink} from '@angular/router';
import {AuthService} from '@core/auth/auth-service';
import {InputChecked} from '@shared/Componenti/Input/input-checked/input-checked';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule, FormsModule, RouterLink, InputChecked],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',
})

export class LoginComponent {

  constructor(private authService: AuthService,
              private router: Router,) {}

  email = '';
  password = '';

  mappaErrori = {
    email: false,
    password: false,
  };

  errore = '';

  onSubmit() {
    this.errore = "";
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

    this.errore = "";

    this.authService.login(email.toLowerCase(), password).subscribe({
      next: (response: any) => {
        this.authService.salvaDatiLogin(response);
        this.router.navigate([response.redirectUrl]);
      },

      error: (error) => {

        if (error.status == 401) {
          this.errore = "Credenziali non valide";
          return;
        }
        this.errore = "Errore di connessione";
      }
    });
  }

  recuperoPassword(): any {
    this.router.navigate(['/recuperoPassword']);
  }
}
