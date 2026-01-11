import {Component, EventEmitter, Output} from '@angular/core';
import {
  MappaGestioneLuoghi
} from '@features/SezioneAdminAziendale/Componenti/modali/mappa-gestione-luoghi/mappa-gestione-luoghi';
import {LuogoDTO} from '@core/models/LuogoDTO';

@Component({
  selector: 'app-modale-obbligo-impostazione-sede',
  imports: [
    MappaGestioneLuoghi
  ],
  templateUrl: './modale-obbligo-impostazione-sede.html',
  styleUrl: './modale-obbligo-impostazione-sede.css',
})
export class ModaleObbligoImpostazioneSede {

  caricamento = false;
  @Output() impostaSede = new EventEmitter<LuogoDTO>();

}
