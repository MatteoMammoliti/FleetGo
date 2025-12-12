import { Component,Input } from '@angular/core';
import {ContenitoreStatisticheNumericheManutezioni} from '@core/models/ContenitoreStatisticheNumericheManutezioni';

@Component({
  selector: 'app-card-dati-manutenzione',
  imports: [],
  templateUrl: './card-dati-manutenzione.html',
  styleUrl: './card-dati-manutenzione.css',
})
export class CardDatiManutenzione {
  @Input() dati:ContenitoreStatisticheNumericheManutezioni={
    interventiConclusi:0,
    attualmenteInOfficina:0
  }

}
