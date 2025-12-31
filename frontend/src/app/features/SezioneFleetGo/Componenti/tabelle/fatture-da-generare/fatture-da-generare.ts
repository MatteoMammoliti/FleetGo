import {Component, EventEmitter, Input, model, Output} from '@angular/core';
import {DashboardFleetGoService} from '@features/SezioneFleetGo/ServiceSezioneFleetGo/dashboardFleetGo-service';
import {FatturaDaGenerareDTO} from '@core/models/FatturaDaGenerareDTO';
import {BottoneChiaro} from '@shared/Componenti/Ui/bottone-chiaro/bottone-chiaro';
import {MessaggioCardVuota} from '@shared/Componenti/Ui/messaggio-card-vuota/messaggio-card-vuota';
import {TemplateTitoloSottotitolo} from '@shared/Componenti/Ui/template-titolo-sottotitolo/template-titolo-sottotitolo';

@Component({
  selector: 'app-fatture-da-generare',
  imports: [
    BottoneChiaro,
    MessaggioCardVuota,
    TemplateTitoloSottotitolo
  ],
  standalone:true,
  templateUrl: './fatture-da-generare.html',
  styleUrl: './fatture-da-generare.css',
})
export class FattureDaGenerare {
  @Input() fatture:FatturaDaGenerareDTO[] = []
  @Output() generaFatturaEvent: EventEmitter<any> = new EventEmitter<any>();

  protected generaFattura(fattura: FatturaDaGenerareDTO) {
    this.generaFatturaEvent.emit(fattura);
  }
}
