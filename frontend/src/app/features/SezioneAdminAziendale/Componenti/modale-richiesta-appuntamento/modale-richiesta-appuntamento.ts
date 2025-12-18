import {Component, EventEmitter, Input, Output} from '@angular/core';
import {OffertaDTO} from '@core/models/offertaDTO.models';
import {DatePipe} from '@angular/common';
import {DashboardService} from '@features/SezioneAdminAziendale/ServiceSezioneAdminAziendale/dashboard-service';

@Component({
  selector: 'app-modale-richiesta-appuntamento',
  imports: [
    DatePipe
  ],
  templateUrl: './modale-richiesta-appuntamento.html',
  styleUrl: './modale-richiesta-appuntamento.css',
})
export class ModaleRichiestaAppuntamento {

  constructor() {}

  @Input() paginaVisibile = false;
  @Input() offertaSelezionata: OffertaDTO = {} as OffertaDTO;
  @Output() chiudiPagina = new EventEmitter<void>();
  @Output() richiediAppuntamento = new EventEmitter<void>();
  @Input() appuntamentoRichiesto = false;
  caricamentoInCorso = false;

  onChiudiPagina() {
    this.paginaVisibile = false;
    this.chiudiPagina.emit();
  }

  inoltraRichiestaAppuntamento() {
    this.caricamentoInCorso = true;
    this.richiediAppuntamento.emit();
  }
}
