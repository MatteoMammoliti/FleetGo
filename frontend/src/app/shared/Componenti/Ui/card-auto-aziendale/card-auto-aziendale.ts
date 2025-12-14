import {Component, EventEmitter, Input, Output} from '@angular/core';
import {VeicoloDTO} from '@core/models/veicoloDTO.model';

@Component({
  selector: 'app-card-auto-aziendale',
  imports: [],
  templateUrl: './card-auto-aziendale.html',
  styleUrl: './card-auto-aziendale.css',
})
export class CardAutoAziendale {
  @Input() veicolo: VeicoloDTO= {} as VeicoloDTO;
  @Output() inviaRichiestaGestione=new EventEmitter<string>;
}
