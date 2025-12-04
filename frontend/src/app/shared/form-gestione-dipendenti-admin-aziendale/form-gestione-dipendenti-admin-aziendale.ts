import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { TabellaBackground } from '@shared/tabella-background/tabella-background';

@Component({
  selector: 'app-form-gestione-dipendenti-admin-aziendale',
  imports: [FormsModule, TabellaBackground],
  standalone: true,
  templateUrl: './form-gestione-dipendenti-admin-aziendale.html',
  styleUrl: './form-gestione-dipendenti-admin-aziendale.css',
})
export class FormGestioneDipendentiAdminAziendale implements OnInit {
  cercaDipendente: string = '';
  filtro: string = '';

  listaDipendenti = [
    { id: '1', nome: 'Luigi', cognome: 'Greco', data: '02/12/2003' },
    { id: '2', nome: 'Andrea', cognome: 'Ambrosanio', data: '17/04/2003' },
    { id: '3', nome: 'Stefano', cognome: 'Greco', data: '10/12/2003' }
    //questi sono i bivona boys, abbiate rispetto :)
  ];

  rimuovi(id: string) {
  }
  ngOnInit() {}
}