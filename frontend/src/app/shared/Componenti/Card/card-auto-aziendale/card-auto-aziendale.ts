import {CommonModule} from '@angular/common';
import {Component, EventEmitter, Input, Output} from '@angular/core';
import {TemplateCardConRiga} from '@shared/Componenti/Card/template-card-con-riga/template-card-con-riga';

@Component({
  selector: 'app-card-auto-aziendale',
  standalone: true,
  imports: [CommonModule, TemplateCardConRiga],
  templateUrl: './card-auto-aziendale.html',
  styleUrl: './card-auto-aziendale.css',
})

export class CardAutoAziendale {
  @Input() veicolo: any;
  @Output() apriDettagli = new EventEmitter<any>();
}
