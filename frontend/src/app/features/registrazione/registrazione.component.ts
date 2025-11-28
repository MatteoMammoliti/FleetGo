import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { FormAutenticazione } from '@shared/form-autenticazione/form-autenticazione';
import { AuthService } from '@core/services/auth-service';
import { DipendenteDTO } from '@models/dipendenteDTO.models';
import {Router} from '@angular/router';

@Component({
  selector: 'app-registration',
  standalone: true,
  imports: [
    FormsModule,
    FormAutenticazione
  ],
  templateUrl: './registrazione.component.html',
  styleUrl: './registrazione.component.css',
})

export class RegistrazioneComponent {
  nome = '';
  cognome = '';
  email = '';
  password = '';
  datanascita = '';
  patente: any = null;

  constructor(private authService: AuthService, private router: Router) {}

  onFileSelected(event: any) {
    this.patente = event.target.files[0];
  }

  onRegistrazione() {
    const user: DipendenteDTO = {
      nomeUtente: this.nome,
      cognomeUtente: this.cognome,
      email: this.email,
      password: this.password,
      dataNascitaUtente: this.datanascita,
      tipoUtente: 'Dipendente',
      patenteAccettata: false
    };

    this.authService.registrazione(user, this.patente).subscribe({
      next: (response) => {
        this.router.navigate(['/login']);
      },
      error: (error) => {
        console.error('Errore durante la registrazione:', error);
      }
    });
  }
}
