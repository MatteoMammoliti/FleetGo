import {Component, Input, input} from '@angular/core';
import {NgClass} from '@angular/common';

@Component({
  selector: 'app-card-stastiche-dashboard',
  imports: [
    NgClass
  ],
  templateUrl: './card-stastiche-dashboard.html',
  styleUrl: './card-stastiche-dashboard.css',
})
export class CardStasticheDashboard {

  @Input() colore: any;
  @Input() icona: any;
  @Input() valore: any;
  @Input() titolo: any;

}
