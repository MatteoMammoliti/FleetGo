import {Component, Input} from '@angular/core';
import {VeicoloDTO} from '@core/models/veicoloDTO.model';
import {LuogoDTO} from '@core/models/luogoDTO.models';
import {VeicoloPrenotazioneDTO} from '@core/models/veicoloPrenotazioneDTO';
import {NgClass} from '@angular/common';

@Component({
  selector: 'app-veicolo-card',
  imports: [
    NgClass
  ],
  templateUrl: './veicolo-card.html',
  styleUrl: './veicolo-card.css',
})
export class VeicoloCard {

  @Input() veicolo:VeicoloPrenotazioneDTO={} as VeicoloPrenotazioneDTO;
  prenota(){}

}

