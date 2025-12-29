import { Component, EventEmitter, Input, Output } from '@angular/core';
import { OffertaDTO } from '@core/models/offertaDTO.models';
import { DatePipe, CommonModule } from '@angular/common';

@Component({
  selector: 'app-modale-richiesta-appuntamento',
  imports: [
    DatePipe,
    CommonModule
  ],
  templateUrl: './modale-richiesta-appuntamento.html',
  styleUrl: './modale-richiesta-appuntamento.css',
})
export class ModaleRichiestaAppuntamento {

  constructor() {}

  @Input() paginaVisibile = false;

  @Input() offertaSelezionata: OffertaDTO | null = null;

  @Input() titoloGenerico: string = "Richiesta Appuntamento";
  @Input() descrizioneGenerica: string = "Hai bisogno di assistenza o vuoi discutere della tua situazione aziendale? Il nostro team è a tua disposizione per trovare la soluzione migliore.";

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
