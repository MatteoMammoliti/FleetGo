import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'registrazione',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './registrazione.component.html',
  styleUrls: ['./registrazione.component.css']
})
export class RegistrazioneComponent {
  nome = '';
  email = '';
  password = '';
  datanascita = '';
  patente: any = null;

  onFileSelected(event: any) {
    this.patente = event.target.files[0];
    console.log("Documento caricato:", this.patente);
  }

  stampaDati() {
    alert("\nNome: " + this.nome + "\nEmail: " + this.email + "\nData di Nascita: " + this.datanascita);
  }
}
