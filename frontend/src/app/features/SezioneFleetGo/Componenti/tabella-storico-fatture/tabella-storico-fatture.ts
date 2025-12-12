import {Component, EventEmitter, Input, Output} from '@angular/core';
import {FatturaDTO} from '@core/models/FatturaDTO.models';

@Component({
  selector: 'app-tabella-storico-fatture',
  imports: [],
  templateUrl: './tabella-storico-fatture.html',
  styleUrl: './tabella-storico-fatture.css',
})

export class TabellaStoricoFatture {

  @Input() fatture: FatturaDTO[] = [];
  @Output() richiestaDownload: EventEmitter<number> = new EventEmitter<number>();

  protected onDownload(numeroFattura: any) {
    this.richiestaDownload.emit(numeroFattura);
  }
}
