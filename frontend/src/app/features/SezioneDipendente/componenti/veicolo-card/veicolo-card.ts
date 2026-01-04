import {Component, EventEmitter, Input, Output} from '@angular/core';
import {VeicoloPrenotazioneDTO} from '@core/models/veicoloPrenotazioneDTO';
import {CurrencyPipe, NgClass} from '@angular/common';

@Component({
  selector: 'app-veicolo-card',
  imports: [
    NgClass,
    CurrencyPipe
  ],
  templateUrl: './veicolo-card.html',
  styleUrl: './veicolo-card.css',
})
export class VeicoloCard {

  @Input() veicolo:VeicoloPrenotazioneDTO={} as VeicoloPrenotazioneDTO;
  @Input() costoStimato:number=0;
  @Output() apriForm= new EventEmitter<VeicoloPrenotazioneDTO>()

  clickPrenota(){
    if(this.veicolo.statoAttuale === 'Non_disponibile') return;
    this.apriForm.emit(this.veicolo);
  }
}
