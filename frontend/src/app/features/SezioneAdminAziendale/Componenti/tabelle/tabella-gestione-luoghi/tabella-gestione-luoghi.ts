import {Component, EventEmitter, Input, Output} from '@angular/core';
import {LuogoDTO} from '@core/models/luogoDTO.models';
import {ANIMAZIONE_TABELLA} from '@shared/Animazioni/animazioneTabella';

@Component({
  selector: 'app-tabella-gestione-luoghi',
  imports: [],
  templateUrl: './tabella-gestione-luoghi.html',
  styleUrl: './tabella-gestione-luoghi.css',
  animations: [ANIMAZIONE_TABELLA]
})
export class TabellaGestioneLuoghi {

  @Input() luoghi: LuogoDTO[] = [];
  @Input() nomeSedeAttuale: string | null | undefined;
  @Output() impostaSede = new EventEmitter<number>;
  @Output() eliminaLuogo = new EventEmitter<number>();

}
