import { CommonModule } from '@angular/common';
import { Component, Input, Output, EventEmitter } from '@angular/core';
import {VeicoloDTO} from '@core/models/veicoloDTO.model';

@Component({
  selector: 'app-card-auto-aziendale',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './card-auto-aziendale.html',
  styleUrl: './card-auto-aziendale.css',
})

export class CardAutoAziendale {
  @Input() veicolo: any;
  @Output() apriDettagli=new EventEmitter<any>();
}
