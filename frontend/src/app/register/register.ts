import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms'; 

@Component({
  selector: 'register',
  standalone: true,
  imports: [FormsModule], 
  templateUrl: './register.html',
  styleUrl: './register.css'
})
export class Register { 
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