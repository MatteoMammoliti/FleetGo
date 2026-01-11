import {Component, EventEmitter, Input, Output} from '@angular/core';
import {OffertaDTO} from '@core/models/OffertaDTO';
import {CommonModule, DatePipe} from '@angular/common';

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

  @Input() offertaSelezionata: OffertaDTO | null = null;

  @Input() titoloGenerico: string = "Richiesta Appuntamento";
  @Input() descrizioneGenerica: string = "Hai bisogno di assistenza o vuoi discutere della tua situazione aziendale? Il nostro team è a tua disposizione per trovare la soluzione migliore.";

  @Output() chiudiPagina = new EventEmitter<void>();
  @Output() richiediAppuntamento = new EventEmitter<void>();

  @Input() appuntamentoRichiesto = false;
  caricamentoInCorso = false;
  chiusuraInCorso = false;

  private eseguiChiusura(eventEmitter: EventEmitter<any>) {
    this.chiusuraInCorso = true;
    setTimeout(() => {
      eventEmitter.emit();
    }, 250);
  }


  chiudiModale() {
    this.eseguiChiusura(this.chiudiPagina);
  }

  confermaModale() {
    this.caricamentoInCorso = true;
    this.richiediAppuntamento.emit();
  }
}
