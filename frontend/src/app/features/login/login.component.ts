import {Component, inject} from '@angular/core';
import {ReactiveFormsModule, Validators} from '@angular/forms';
import{FormBuilder} from '@angular/forms';
import {Router} from '@angular/router';
import {FormAutenticazione} from '@shared/form-autenticazione/form-autenticazione';
import {AuthService} from '@core/services/auth-service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    FormAutenticazione
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',
})

export class LoginComponent {
  private fb = inject(FormBuilder);
  private router = inject(Router);
  private authService = inject(AuthService);

  errorePassword = "";
  erroreEmail = "";
  erroreCredenziali = "";

  loginForm = this.fb.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required]]
  });

  onSubmit() {
    if (this.loginForm.invalid) {
      if (this.loginForm.get('email')?.invalid) {
        this.erroreEmail = "Email non valida";
      } else {
        this.erroreEmail = "";
      }
      if (this.loginForm.get('password')?.invalid) {
        this.errorePassword = "Password non valida";
      } else {
        this.errorePassword = "";
      }
      return;
    }

    this.errorePassword = "";
    this.erroreEmail = "";

    const email = this.loginForm.value.email!;
    const password = this.loginForm.value.password!;

    console.log(email, password);

    this.authService.login(email, password).subscribe({
      next: (response: any) => {
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
        this.erroreCredenziali = "Credenziali non valide";
      }
    });
  }
}
