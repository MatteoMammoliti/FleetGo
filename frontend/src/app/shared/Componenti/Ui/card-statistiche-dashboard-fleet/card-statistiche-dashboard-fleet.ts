import {booleanAttribute, Component, Input} from '@angular/core';
import {NgClass} from '@angular/common';

@Component({
  selector: 'app-card-statistiche-dashboard-fleet',
  imports: [],
  standalone:true,
  templateUrl: './card-statistiche-dashboard-fleet.html',
  styleUrl: './card-statistiche-dashboard-fleet.css',
})
export class CardStatisticheDashboardFleet {
  @Input() titolo:string ="";
  @Input() valore:string |number =0;
  @Input() descrizione:string ="";
  @Input() icona:string ="";
  @Input() coloreSfondo:string = 'bg-primary'
  @Input() percentuale:string="";
}
