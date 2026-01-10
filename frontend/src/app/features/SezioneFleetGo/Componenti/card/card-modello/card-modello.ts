import {Component, EventEmitter, Input, Output} from '@angular/core';
import {ModelloDTO} from '@core/models/ModelloDTO';
import {TemplateCardConRiga} from '@shared/Componenti/Card/template-card-con-riga/template-card-con-riga';

@Component({
  selector: 'app-card-modello',
  imports: [
    TemplateCardConRiga
  ],
  templateUrl: './card-modello.html',
  styleUrl: './card-modello.css',
})

export class CardModello {

  @Input() modello: ModelloDTO = {} as ModelloDTO;
  @Output() elimina = new EventEmitter<number>();
}
