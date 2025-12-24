import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {DipendenteDTO} from '@core/models/dipendenteDTO.models';
import {RichiestaNoleggioDTO} from '@core/models/richiestaNoleggioDTO.models';
import {DatePipe, NgClass} from '@angular/common';

@Component({
  selector: 'app-modale-dettagli-dipendente',
  imports: [
    DatePipe,
    NgClass
  ],
  templateUrl: './modale-dettagli-dipendente.html',
  styleUrl: './modale-dettagli-dipendente.css',
})

export class ModaleDettagliDipendente implements OnInit{

  @Input() paginaVisibile = false;
  @Input() dipendente: DipendenteDTO | null = null;
  @Input() storicoNoleggi: RichiestaNoleggioDTO[] = []
  @Output() chiudiPagina = new EventEmitter<void>();
  approvazioneInCorso = false;
  tabAttiva = "INFO";

  ngOnInit() {}

  get iniziali(): string {
    if (!this.dipendente || !this.dipendente.nomeUtente || !this.dipendente.cognomeUtente) {
      return '';
    }
    return (this.dipendente.nomeUtente.charAt(0) + this.dipendente.cognomeUtente.charAt(0)).toUpperCase();
  }

  impostaTab(tipologia: string) {
    this.tabAttiva = tipologia;
  }
}
