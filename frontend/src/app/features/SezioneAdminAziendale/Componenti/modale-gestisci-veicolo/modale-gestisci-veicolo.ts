import {Component, EventEmitter, Input, Output} from '@angular/core';
import {VeicoloDTO} from '@core/models/veicoloDTO.model';
import {LuogoDTO} from '@core/models/luogoDTO.models';

@Component({
  selector: 'app-modale-gestisci-veicolo',
  imports: [],
  templateUrl: './modale-gestisci-veicolo.html',
  styleUrl: './modale-gestisci-veicolo.css',
})
export class ModaleGestisciVeicolo {

  @Input() veicolo: VeicoloDTO = {} as VeicoloDTO;
  @Input() visibile:Boolean = false;
  @Input() listaLuoghi:LuogoDTO[] = [];
  @Output() chiudi=new EventEmitter<void>;

  copiaVeicolo ={...this.veicolo}

}
