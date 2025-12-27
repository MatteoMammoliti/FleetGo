import {Component, EventEmitter, Input, Output} from '@angular/core';
import {ModelloDTO} from '@core/models/ModelloDTO';

@Component({
  selector: 'app-card-modello',
  imports: [],
  templateUrl: './card-modello.html',
  styleUrl: './card-modello.css',
})

export class CardModello {

  @Input() modello: ModelloDTO = {} as ModelloDTO;
  @Input() erroreEliminazione = "";
  @Output() elimina = new EventEmitter<number>();
}
