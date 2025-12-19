import {Component, Input} from '@angular/core';
import {StatisticheDipendenteDTO} from '@core/models/StatisticheDipendenteDTO';
import {DecimalPipe} from '@angular/common';

@Component({
  selector: 'app-statistiche-dipendente',
  imports: [
    DecimalPipe
  ],
  standalone:true,
  templateUrl: './statistiche-dipendente.html',
  styleUrl: './statistiche-dipendente.css',
})
export class StatisticheDipendente {
  @Input() statistiche:StatisticheDipendenteDTO={
    oreTotaliDiGuida:1,
    viaggiMensili:1
  };
}
