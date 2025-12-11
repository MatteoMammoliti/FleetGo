import {Component, OnInit, Input, Output, EventEmitter} from '@angular/core';
import { FormsModule } from '@angular/forms';
import {DipendenteDTO} from '@core/models/dipendenteDTO.models';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-form-gestione-dipendenti-admin-aziendale',
  imports: [FormsModule, RouterLink],
  standalone: true,
  templateUrl: './form-gestione-dipendenti-admin-aziendale.html',
  styleUrl: './form-gestione-dipendenti-admin-aziendale.css',
})
export class FormGestioneDipendentiAdminAziendale {
  cercaDipendente: string = '';
  @Input() listaDipendenti: DipendenteDTO[] = [];
  @Output() richiestaRimozioneDipendente=new EventEmitter<number>();


  get dipendentiFiltrati(): DipendenteDTO[] {
    if (!this.cercaDipendente) {
      return this.listaDipendenti;
    }

    return this.listaDipendenti.filter(d =>
      (d.nomeUtente && d.nomeUtente.toLowerCase().includes(this.cercaDipendente.toLowerCase())) ||
      (d.cognomeUtente && d.cognomeUtente.toLowerCase().includes(this.cercaDipendente.toLowerCase()))
    );
  }


  rimuoviDipendente(idDipendente:number | undefined) {
    this.richiestaRimozioneDipendente.emit(idDipendente);
  }





}
