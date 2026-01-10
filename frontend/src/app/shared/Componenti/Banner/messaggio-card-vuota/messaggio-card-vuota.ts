import {Component, Input} from '@angular/core';

@Component({
  selector: 'app-messaggio-card-vuota',
  imports: [],
  templateUrl: './messaggio-card-vuota.html',
  styleUrl: './messaggio-card-vuota.css',
})
export class MessaggioCardVuota {
  @Input() testoPrimario = "";
  @Input() testoSecondario = "";
  @Input() icona = "";
  @Input() tagliaIcona = "xl";
}
