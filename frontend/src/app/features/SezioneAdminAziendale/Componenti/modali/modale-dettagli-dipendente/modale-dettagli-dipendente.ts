import {Component, EventEmitter, Input, LOCALE_ID, NgModule, OnInit, Output} from '@angular/core';
import {DipendenteDTO} from '@core/models/dipendenteDTO.models';
import {RichiestaNoleggioDTO} from '@core/models/richiestaNoleggioDTO.models';
import {DatePipe, DecimalPipe, NgClass, registerLocaleData} from '@angular/common';
import {IconaStato} from '@shared/Componenti/Ui/icona-stato/icona-stato';
import {MessaggioCardVuota} from '@shared/Componenti/Ui/messaggio-card-vuota/messaggio-card-vuota';

@Component({
  selector: 'app-modale-dettagli-dipendente',
  imports: [
    DatePipe,
    NgClass,
    IconaStato,
    DecimalPipe,
    MessaggioCardVuota
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
