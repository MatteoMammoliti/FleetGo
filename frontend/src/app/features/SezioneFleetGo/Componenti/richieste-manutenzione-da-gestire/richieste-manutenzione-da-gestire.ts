import {Component, EventEmitter, Input, Output} from '@angular/core';
import {DashboardFleetGoService} from '@features/SezioneFleetGo/ServiceSezioneFleetGo/dashboardFleetGo-service';
import {RichiestaManutenzioneDTO} from '@core/models/RichiestaManutenzioneDTO';
import {DatePipe, SlicePipe} from '@angular/common';

@Component({
  selector: 'app-richieste-manutenzione-da-gestire',
  imports: [
    DatePipe,
    SlicePipe
  ],
  templateUrl: './richieste-manutenzione-da-gestire.html',
  styleUrl: './richieste-manutenzione-da-gestire.css',
})
export class RichiesteManutenzioneDaGestire {
  constructor() {}
  @Input() richiesteManutenzioneDaGestire:RichiestaManutenzioneDTO[] = []
  @Output() inviaRichiesta:EventEmitter<number>=new EventEmitter<number>();

  gestisciRichiesta(idManutenzione:number){
    this.inviaRichiesta.emit(idManutenzione);
  }

}
