import {Component, Input, input} from '@angular/core';
import {NgClass} from '@angular/common';

@Component({
  selector: 'app-card-statistiche-dashboard',
  imports: [
    NgClass
  ],
  templateUrl: './card-statistiche-dashboard.html',
  styleUrl: './card-statistiche-dashboard.css',
})
export class CardStatisticheDashboard {

  @Input() colore: any;
  @Input() icona: any;
  @Input() valore: any;
  @Input() titolo: any;

}
